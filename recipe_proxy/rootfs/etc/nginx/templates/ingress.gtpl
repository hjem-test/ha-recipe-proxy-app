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

        # Buffer the entire response so Lua can process it completely
        proxy_buffering on;
        proxy_buffer_size 128k;
        proxy_buffers 8 128k;

        # Use Lua to rewrite HTML responses
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

                -- Buffer the entire response body
                if not ngx.ctx.buffered then
                    ngx.ctx.buffered = ""
                end

                if ngx.arg[1] then
                    ngx.ctx.buffered = ngx.ctx.buffered .. ngx.arg[1]
                    ngx.arg[1] = nil
                end

                -- Process when we have the complete response
                if ngx.arg[2] then
                    local body = ngx.ctx.buffered

                    -- Replace existing Angular base href with ingress path
                    body = string.gsub(body, '<base href="/">', '<base href="' .. ingress_path .. '/">')
                    body = string.gsub(body, "<base href='/'>", "<base href='" .. ingress_path .. "/'>")
                    body = string.gsub(body, '<base href="">', '<base href="' .. ingress_path .. '/">')

                    -- If no base tag exists, inject one
                    if not string.find(body, "<base") then
                        body = string.gsub(body, "<head>", '<head><base href="' .. ingress_path .. '/">')
                        body = string.gsub(body, "<HEAD>", '<HEAD><base href="' .. ingress_path .. '/">')
                    end

                    -- Rewrite absolute paths in script and link tags
                    body = string.gsub(body, 'src="/', 'src="' .. ingress_path .. '/')
                    body = string.gsub(body, 'href="/', 'href="' .. ingress_path .. '/')
                    body = string.gsub(body, "src='/", "src='" .. ingress_path .. "/")
                    body = string.gsub(body, "href='/", "href='" .. ingress_path .. "/")

                    ngx.arg[1] = body
                    ngx.arg[2] = true
                end
            end
        }

        include /etc/nginx/includes/proxy_params.conf;
    }
}
