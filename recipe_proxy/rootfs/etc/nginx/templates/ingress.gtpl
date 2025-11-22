server {
    listen 5000 default_server;

    include /etc/nginx/includes/server_params.conf;

    allow   172.30.32.2;
    deny    all;

    location / {
        # Proxy to backend
        proxy_pass {{ .server }}/;

        # Set headers
        proxy_set_header X-Ingress-Path {{ .entry }};
        {{ if .proxy_pass_host }}
        proxy_set_header Host $http_host;
        {{ end }}
        {{ if .proxy_pass_real_ip }}
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Real-IP $remote_addr;
        {{ end }}

        # Disable compression so rewriting works
        proxy_set_header Accept-Encoding "";

        # Use Lua to rewrite HTML responses with base tag
        header_filter_by_lua_block {
            local content_type = ngx.header["Content-Type"]
            if content_type and string.find(content_type, "text/html") then
                ngx.header.content_length = nil
            end
        }

        body_filter_by_lua_block {
            local content_type = ngx.header["Content-Type"]
            if content_type and string.find(content_type, "text/html") then
                local ingress_path = "{{ .entry }}"
                local chunk = ngx.arg[1]
                if chunk then
                    -- Replace existing Angular base href with ingress path
                    chunk = string.gsub(chunk, '<base href="/">', '<base href="' .. ingress_path .. '/">')
                    chunk = string.gsub(chunk, "<base href='/'>", "<base href='" .. ingress_path .. "/'>")
                    chunk = string.gsub(chunk, '<base href="">', '<base href="' .. ingress_path .. '/">')

                    -- If no base tag exists, inject one
                    if not string.find(chunk, "<base") then
                        chunk = string.gsub(chunk, "<head>", '<head><base href="' .. ingress_path .. '/">')
                        chunk = string.gsub(chunk, "<HEAD>", '<HEAD><base href="' .. ingress_path .. '/">')
                    end

                    -- Rewrite absolute paths in script and link tags
                    chunk = string.gsub(chunk, 'src="/', 'src="' .. ingress_path .. '/')
                    chunk = string.gsub(chunk, 'href="/', 'href="' .. ingress_path .. '/')
                    chunk = string.gsub(chunk, "src='/", "src='" .. ingress_path .. "/")
                    chunk = string.gsub(chunk, "href='/", "href='" .. ingress_path .. "/")

                    ngx.arg[1] = chunk
                end
            end
        }

        include /etc/nginx/includes/proxy_params.conf;
    }
}
