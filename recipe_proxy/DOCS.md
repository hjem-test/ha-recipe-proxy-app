# Configuration

This add-on creates a proxy to a recipe application server running separately from Home Assistant, allowing you to access it through the Home Assistant sidebar.

**Note:** This add-on does not run your recipe application itself. It only provides a proxy to an existing instance.

## Options

### Option: `frontend_server`

The URL of your recipe application's frontend server. This should include the protocol (http or https), hostname or IP address, and port number. Everything except `/api/` requests is proxied here.

**Format:** `http[s]://host:port`

**Examples:**
- `http://192.168.0.175:4200`
- `http://recipe-app.local:8080`

### Option: `backend_server`

The URL of your recipe application's backend API server, in the same format. All `/api/` requests are proxied here.

**Examples:**
- `http://192.168.0.175:5000`
- `https://192.168.1.100:443`

### Option: `api_key`

An API key from the recipe app, so you do not have to log in inside Home Assistant. An admin makes the key in the recipe app under **Admin → API keys** and chooses which user it logs in as; the key is shown only once, so copy it then. It starts with `rak_`.

With a key set, the proxy adds it to every API request and the app logs itself in as the key's user when it opens in Home Assistant. Everyone who can open the Recipes panel in Home Assistant then uses that account, so pick the user accordingly. If you log out inside Home Assistant, you can log in as another user; **Log in with Home Assistant** on the login page uses the key again, and so does a new browser tab.

Leave it empty to log in with a username and password as before. Deleting the key in the app ends the sessions it started.

**Default:** empty

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
