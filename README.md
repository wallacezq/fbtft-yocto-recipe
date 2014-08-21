Yocto Recipe to install FBTFT driver for Intel Galileo (gen1 and gen2)
======================================================================

This recipe will install fbtft driver (Commit #71994224c5ed951eab7ca9da2c919456d1632d15) with patch to disable dma.
Full fbtft driver source can be downloaded from https://github.com/notro/fbtft.

*The usage below is specific to Intel Galileo Gen 1 and Gen 2 only*

Pre-requisite
=============

Please ensure the yocto kernel has the following config options enabled before proceed:
* FB_SYS_FILLRECT
* FB_SYS_COPYAREA
* FB_SYS_IMAGEBLIT
* FB_SYS_FOPS
* FB_DEFERRED_IO
* FB_BACKLIGHT

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
3. Rebuild the rootfs and transfer the image into the SDCard:
```code
~$ bitbake image-spi #for spi image
or
~$ bitbake image-full #for full featured yocto BSP.
```

Hardware Wiring Recommendation
==============================
Fbtft driver do not work well with the GPIOs connected to the I2C expander. 
This guide assume Adafruit 2.8" TFT shield v2 (based on ili9341 chip) is used.
Please refer to [Adafruit Website](https://learn.adafruit.com/adafruit-2-8-tft-touch-shield-v2/overview) for more details.
Please re-route TFT_DC (IO9) and TFT_CS (IO10) to Digital IO2 and IO3 as they connect directly to Quark SoC.


Setting-Up GPIO
================

Create a shell script to initialize the GPIO. Execute the script before loading FBTFT driver module.

```code
#!/bin/sh

#GPIO29 is IO3_MUX
echo -n "29" > /sys/class/gpio/export
#GPIO30 is IO2_MUX
echo -n "30" / sys/class/gpio/export

echo -n "out" > /sys/class/gpio/gpio29/direction
echo -n "out" > /sys/class/gpio/gpio30/direction

#output low to wire IO2 & IO3 to Quark SoC's GPIO
echo -n "0" > /sys/class/gpio/gpio29/value
echo -n "0" > /sys/class/gpio/gpio30/value

#initialize CS pin and drive the pin low.
echo -n "7" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio7/direction
echo -n "0" > /sys/class/gpio/gpio7/value

#note: TFT_DC pin will be initialized by the fbtft driver

```

Loading the Kernel Module from Yocto Shell:
===========================================

1. Execute the following shell commands in the shell to load fbtft driver:
```
~# modprobe fbtft_device custom name=fb_ili9341 buswidth=8 gpios=dc:6 width=320 height=240 busnum=1 bgr=1 speed=4000000
~# modprobe fb_ili9341
```
2. Verify that /dev/fb0 exists and execute "cat /dev/urandom > /dev/fb0" in the priviledged shell.
3. You should see random dots printed on the tft device.
