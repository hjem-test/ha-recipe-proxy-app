# Home Assistant Recipe Proxy Add-on

![Supports aarch64 Architecture][aarch64-shield]
![Supports amd64 Architecture][amd64-shield]
![Supports armhf Architecture][armhf-shield]
![Supports armv7 Architecture][armv7-shield]
![Supports i386 Architecture][i386-shield]

## About

This add-on creates a proxy to a recipe application server run separately from Home Assistant so that you can have the benefit of access in the sidebar without running your recipe app as an add-on.

**Note:** This add-on does not run your recipe application itself. It only provides a proxy to an existing recipe application running elsewhere on your network.

## Installation

1. Add this repository to your Home Assistant add-on store
2. Install the "Recipe Proxy" add-on
3. Configure the add-on with your recipe application's URL
4. Start the add-on
5. Access your recipe application through the Home Assistant sidebar

## Configuration

**frontend_server:** The URL of your frontend application server (e.g., `http://192.168.0.175:4200`)

**backend_server:** The URL of your backend API server (e.g., `http://192.168.0.175:5000`)

**proxy_pass_host:** Forward the Home Assistant hostname to your application servers. Set to `false` if your application is behind an SSL proxy like Traefik or Caddy. (Default: `true`)

**proxy_pass_real_ip:** Forward the client's real IP address to your application servers. Set to `false` if your application restricts access based on IP addresses through an upstream proxy. (Default: `true`)

### How It Works

The proxy automatically routes requests to the appropriate server:
- `/api/*` requests are proxied to the **backend_server**
- All other requests (frontend assets, HTML, etc.) are proxied to the **frontend_server**

## Support

For issues, please visit the [GitHub repository](https://github.com/Ephis/ha-recipe-proxy-app/issues).

[aarch64-shield]: https://img.shields.io/badge/aarch64-yes-green.svg
[amd64-shield]: https://img.shields.io/badge/amd64-yes-green.svg
[armhf-shield]: https://img.shields.io/badge/armhf-yes-green.svg
[armv7-shield]: https://img.shields.io/badge/armv7-yes-green.svg
[i386-shield]: https://img.shields.io/badge/i386-yes-green.svg
