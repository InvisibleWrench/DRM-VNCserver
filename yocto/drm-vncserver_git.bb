DESCRIPTION = "VNC server running on top of DRM"
HOMEPAGE = "https://github.com/InvisibleWrench/DRM-VNCserver"
LICENSE = "CLOSED"

DEPENDS = "libvncserver libdrm drm"
RDEPENDS_${PN} = "libvncserver libdrm drm"
SRC_URI = "git://github.com/InvisibleWrench/DRM-VNCserver.git;branch=main;protocol=https \
           file://xf86drm.patch;patchdir=../recipe-sysroot/usr/include/ \
           file://xf86drmMode.patch;patchdir=../recipe-sysroot/usr/include/ \
           "
           
# SRCREV = "f706209c1d165f9b418aef284adc85bae2d49322"
SRCREV = "${AUTOREV}"
PV = "${VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig


# Systemd service

inherit systemd features_check
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "drmvnc.service"

SRC_URI += " file://drmvnc.service"

do_install:append() {
  install -d ${D}/etc/systemd/system/multi-user.target.wants
  install -m 0644 ${WORKDIR}/drmvnc.service ${D}/etc/systemd/system/drmvnc.service
}
FILES:${PN} += "${systemd_system_unitdir}/drmvnc.service"

REQUIRED_DISTRO_FEATURES = "systemd"
