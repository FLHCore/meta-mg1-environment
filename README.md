# meta-mg1-environment

This layer provides universal feature extensions for Yocto environments, focusing on web server capabilities. It's designed to work with any base configuration without requiring specific distro settings.

---

## 📦 Dependencies

This layer depends on:

- `meta-openembedded/meta-webserver` (for nginx support)
- `meta-hsw-bringup` (base layer)

---

## 🎯 Design Philosophy

This layer follows the principle of **composition over inheritance**:
- **Universal compatibility** - Works with any base layer (poky, nilm, etc.)
- **Modular features** - Easy to enable/disable specific features
- **Minimal dependencies** - Only depends on essential layers
- **Clear separation** - Each feature is independent

---

## 1. Clone Required Layers
cd to `src`
```bash
# Main layer
git clone git@github.com:FLHCore/meta-mg1-environment.git

# Dependencies
git clone git@github.com:FLHCore/meta-hsw-bringup
git clone git://git.openembedded.org/meta-openembedded.git -b kirkstone
```

**Important: If nginx 1.24.0 is not available, update meta-openembedded to the latest kirkstone version:**
```bash
# Remove old meta-openembedded
rm -rf src/meta-openembedded

# Clone the latest kirkstone version
git clone --branch kirkstone git://git.openembedded.org/meta-openembedded.git
```

## 2. Add Layers to bblayers.conf
in project `root folder`/build

**Important: Add layers in the correct order (dependencies first):**

```bash
# 1. First add the dependency layer
bitbake-layers add-layer ../src/meta-openembedded/meta-webserver

# 2. Then add our layer
bitbake-layers add-layer ../src/meta-mg1-environment
```

**Verify layers are added correctly:**
```bash
bitbake-layers show-layers
```

## 3. Set DISTRO and MACHINE

You can use ANY distro configuration:

### Option A: Use nilm distro
```bash
export DISTRO=nilm
export MACHINE=genio-350-evk
```

### Option B: Use poky distro
```bash
export DISTRO=poky
export MACHINE=genio-350-evk
```

### Option C: Use custom distro
```bash
export DISTRO=your-custom-distro
export MACHINE=genio-350-evk
```

## 4. Build the Image
```bash
# Test build nginx first (recommended)
bitbake nginx

# Check nginx version
bitbake -e nginx | grep ^PV=

# Build the full image
bitbake mg1-image
```

## 5. Output Image Location
```
build/tmp/deploy/images/genio-350-evk/
```

Look for:

- .wic.img (rootfs)
- fitImage (kernel)
- other generated boot files

✅ Included Features

- **Nginx web server (1.24.0)** - Latest stable version
- Nginx stream module (TCP/UDP proxying)
- Nginx headers-more module (advanced header manipulation)
- Nginx auth-request module (authentication)
- Nginx gunzip module (compression)
- SSH server support
- ntpdate for time sync

## Nginx Configuration

The nginx server is configured with:
- HTTP and HTTPS support
- Stream module for TCP/UDP proxying
- Headers-more module for advanced header manipulation
- Auth-request module for authentication
- Gunzip module for compression
- Custom configuration in `/etc/nginx/conf.d/mg1-nginx.conf`

## Usage

After booting the image:
1. SSH to the device: `ssh root@<device-ip>`
2. Start nginx: `systemctl start nginx`
3. Enable nginx: `systemctl enable nginx`
4. Check status: `systemctl status nginx`

## 🔄 Version Management

This layer automatically upgrades nginx to version 1.24.0 (from the default 1.20.1) by:
- Setting `PREFERRED_VERSION_nginx = "1.24.0"`
- Overriding `DEFAULT_PREFERENCE = "1"` to ensure higher priority

To check which version is being used:
```bash
bitbake -e nginx | grep -E "^(PV|PREFERRED_VERSION_nginx)"
```

## 🧩 Extending Features

To add new features, create new packagegroups:

1. Create `recipes-core/packagegroups/packagegroup-mg1-<feature>.bb`
2. Add packages to RDEPENDS
3. Add to mg1-image.bb as commented option
4. Update this README.md

## 🎨 Universal Compatibility

This layer is designed to work with:
- Any base layer (poky, nilm, custom)
- Any machine configuration
- Any distro configuration
- Any Yocto version (kirkstone+)

The key is that it only adds features without changing the base configuration.

## 🔧 Troubleshooting

If you encounter layer dependency errors:
1. Make sure `meta-webserver` is added before `meta-mg1-environment`
2. Check that all layers are in the correct order
3. Verify layer compatibility with `bitbake-layers show-layers`

If nginx version issues:
1. Check `bitbake -e nginx | grep -E "^(PV|PREFERRED_VERSION_nginx|DEFAULT_PREFERENCE)"`
2. Clean and rebuild: `bitbake -c clean nginx && bitbake nginx`
3. **If nginx 1.24.0 is not found, update meta-openembedded:**
   ```bash
   cd src
   rm -rf meta-openembedded
   git clone --branch kirkstone git://git.openembedded.org/meta-openembedded.git
   ``` 