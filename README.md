Yocto Recipe to install FBTFT driver for Intel Galileo (gen1 and gen2)
======================================================================

This recipe will install fbtft driver (Commit #71994224c5ed951eab7ca9da2c919456d1632d15) with patch to disable dma.
Full fbtft driver source can be downloaded from https://github.com/notro/fbtft.

*The usage below is specific to Intel Galileo Gen 1 and Gen 2 only*

Usage:
======

1. Copy the contents in the "recipe/*" folder to */meta-clanton_vX.Y.Z/meta-clanton-distro/recipes-kernel/*.
2. Append the line **IMAGE_INSTALL += "fbtft-driver"** to the files listed below:
```
/meta-clanton_vX.Y.Z/meta-clanton-distro/recipes-core/images/image-full.bb
/meta-clanton_vX.Y.Z/meta-clanton-distro/recipes-core/images/image-spi.bb
```
2. Once done, enter the following command in the yocto build environment:
```code
~$ bitbake fbtft-driver -c build
```
3. Rebuild the rootfs:
```code
~$ bitbake image-spi #for spi image
or
~$ bitbake image-full #for full featured yocto BSP.
```

Loading the Kernel Module from Yocto Shell:
===========================================

The following steps show how to load the fbtft driver to work with the Adafruit 2.8 tft touch shield v2. 
Please refer to fbtft wiki if you have a different tft device.

``code
~$ ...
```