DESCRIPTION = "fbtft  Kernel Driver Sample"
HOMEPAGE = "https://github.com/notro/fbtft.git"
SECTION = "kernel/modules"
PRIORITY = "optional"
LICENSE = "CLOSED"
RDEPENDS_fbtft = "kernel (${KERNEL_VERSION})"
DEPENDS = "virtual/kernel"
PR = "r0"

#SRC_URI = "https://github.com/robopeak/rpusbdisp.git"
#SRC_URI = "https://github.com/robopeak/rpusbdisp/archive/master.zip"
#SRC_URI = "https://github.com/notro/fbtft/archive/master.zip \
#	   file://dma_disable.patch"

SRCREV = "71994224c5ed951eab7ca9da2c919456d1632d15"
SRC_URI = "git://github.com/notro/fbtft.git;protocol=http \
	   file://dma_disable.patch"

S="${WORKDIR}/git/"

inherit module

do_compile() {
  unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS CC LD CPP
  oe_runmake 'MODPATH="${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/video/fbtft" ' \
             'KDIR="${STAGING_KERNEL_DIR}"' \
             'KERNEL_VERSION="${KERNEL_VERSION}"' \
             'CC="${KERNEL_CC}"' \
             'LD="${KERNEL_LD}"'
}


do_install() {
   install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/video/fbtft

#   for driver_ko in ${S}/$
   #install -m 0644 ${S}/fbtft*${KERNEL_OBJECT_SUFFIX} ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/video/fbtft
   install -m 0644 ${S}/f*${KERNEL_OBJECT_SUFFIX} ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/video/fbtft
}

SRC_URI[md5sum] = "01c531fe2a18fede560b26b9bd97726f"
SRC_URI[sha256sum] = "0beed940ae281c4e48425faa5b07fb774cd06a31c619d80e9dfff2e8d9a0507c"



#LIC_FILES_CHKSUM = "file://../../LICENSE;md5=4326bf94c79e51e9d3f8d7c106fa0fde"
