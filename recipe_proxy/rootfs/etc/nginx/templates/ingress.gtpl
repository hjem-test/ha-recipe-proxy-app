server {
    listen 5000 default_server;

    include /etc/nginx/includes/server_params.conf;

    allow   172.30.32.2;
    deny    all;

    # Proxy API requests to backend server
    location /api/ {
        # Proxy to backend API server
        proxy_pass {{ .backend_server }};

        # Set headers
        proxy_set_header X-Ingress-Path {{ .entry }};
        {{ if .proxy_pass_host }}
        proxy_set_header Host $http_host;
        {{ end }}
        {{ if .proxy_pass_real_ip }}
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Real-IP $remote_addr;
        {{ end }}

        include /etc/nginx/includes/proxy_params.conf;
    }

    # Proxy everything else to frontend server
    location / {
        # Proxy to frontend server
        proxy_pass {{ .frontend_server }};

        # Set headers
        proxy_set_header X-Ingress-Path {{ .entry }};
        {{ if .proxy_pass_host }}
        proxy_set_header Host $http_host;
        {{ end }}
        {{ if .proxy_pass_real_ip }}
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Real-IP $remote_addr;
        {{ end }}

        # Disable compression for sub_filter to work
        proxy_set_header Accept-Encoding "";

        # Buffer the response
        proxy_buffering on;
        proxy_buffer_size 128k;
        proxy_buffers 8 128k;

        # Use sub_filter to rewrite paths (built into nginx core)
        sub_filter_types text/html application/javascript text/javascript;
        sub_filter_once off;
        sub_filter_last_modified off;

        # Replace Angular base href tag
        sub_filter '<base href="/">' '<base href="{{ .entry }}/">';
        sub_filter '<base href="/angular/">' '<base href="{{ .entry }}/">';

        # Rewrite absolute paths in HTML
        sub_filter 'src="/' 'src="{{ .entry }}/';
        sub_filter 'href="/' 'href="{{ .entry }}/';
        sub_filter "src='/" "src='{{ .entry }}/";
        sub_filter "href='/" "href='{{ .entry }}/";

        # Rewrite absolute URLs to relative URLs in JavaScript (base href will resolve them)
        # This prevents double ingress path when Angular HTTP client uses base href
        sub_filter '"/version.json"' '"version.json"';
        sub_filter "'/version.json'" "'version.json'";
        sub_filter '"/assets/' '"assets/';
        sub_filter "'/assets/" "'assets/";
        sub_filter '"/api/' '"api/';
        sub_filter "'/api/" "'api/";
        sub_filter '`/api/' '`api/';
        sub_filter '`/assets/' '`assets/';
        sub_filter '`/version.json' '`version.json';

        include /etc/nginx/includes/proxy_params.conf;
    }
}
