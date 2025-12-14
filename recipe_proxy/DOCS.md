# Configuration

This add-on creates a proxy to a recipe application server running separately from Home Assistant, allowing you to access it through the Home Assistant sidebar.

**Note:** This add-on does not run your recipe application itself. It only provides a proxy to an existing instance.

## Options

### Option: `server`

The URL of your recipe application server. This should include the protocol (http or https), hostname or IP address, and port number.

**Format:** `http[s]://host:port`

**Examples:**
- `http://recipe-app.local:8080`
- `http://192.168.1.100:8080`
- `https://192.168.1.100:443`

### Option: `proxy_pass_host`

This option determines whether to forward the Home Assistant hostname to your recipe application server.

**Default:** `true`

Set this to `false` if your recipe application is behind an SSL proxy like Traefik or Caddy.

### Option: `proxy_pass_real_ip`

This option determines whether to forward the client's real IP address to your recipe application server.

**Default:** `true`

Set this to `false` if your recipe application restricts access based on specific IP addresses through an upstream proxy.

## Fullscreen Mode (Hiding Home Assistant Header)

By default, the add-on displays within Home Assistant's interface, which includes the sidebar and header navigation. The ingress system doesn't provide a built-in way to hide the header. If you want a fullscreen experience without the Home Assistant header, you have the following options:

### Recommended: hass_ingress Integration

Install the [hass_ingress](https://github.com/lovelylain/hass_ingress) integration from HACS, which provides a `ui_mode` option to hide the header:

1. Install [HACS](https://hacs.xyz/) if you haven't already
2. In HACS, add the custom repository: `https://github.com/lovelylain/hass_ingress`
3. Install the integration and restart Home Assistant
4. Add to your `configuration.yaml`:

```yaml
ingress:
  recipes:
    work_mode: hassio
    url: recipe-proxy
    ui_mode: replace
    title: Recipes
    icon: mdi:food
```

The `ui_mode: replace` setting hides the Home Assistant header.

### Alternative Options

1. **Kiosk Mode HACS Integration** - Install the [kiosk-mode](https://github.com/NemesisRE/kiosk-mode) integration to hide the header and sidebar.

2. **Browser Kiosk Mode** - Press F11 in your browser for fullscreen mode.

3. **Direct Access** - Access your recipe app directly at its URL (e.g., `http://192.168.0.175:4200`) instead of through Home Assistant.

## Requirements

- Your recipe application must be running and accessible on your network
- The URL must be reachable from the Home Assistant host

## Getting Help

If you have issues with this add-on, please visit the [GitHub repository](https://github.com/Ephis/ha-recipe-proxy-app/issues) to submit an issue.
