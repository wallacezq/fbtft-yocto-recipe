Yocto Recipe to install FBTFT driver for Intel Galileo (gen1 and gen2)
======================================================================

This recipe will install fbtft driver (Commit #71994224c5ed951eab7ca9da2c919456d1632d15) with patch to disable dma.
Full fbtft driver source can be downloaded from https://github.com/notro/fbtft.

*The usage below is specific to Intel Galileo Gen 1 & Gen 2 only.*


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

This guide assume Adafruit 2.8" TFT shield v2 (based on ili9341 chip) is used.
Please refer to [Adafruit Website](https://learn.adafruit.com/adafruit-2-8-tft-touch-shield-v2/overview) for more details.
This guide assumes that the default pins are used - TFT_DC (Arduino #IO9) and TFT_CS (Arduino #IO10).
~~Fbtft driver do not work well with the GPIOs connected to the I2C expander.~~ 
~~Please re-route TFT_DC (IO9) and TFT_CS (IO10) to Digital IO2 and IO3 as they connect directly to Quark SoC.~~


Setting-Up GPIO (Valid for Intel Galileo Gen1 board only)
=========================================================

Create a shell script to initialize the GPIO. Execute the script before loading FBTFT driver module.
Please refer to [Malinov's Website](http://www.malinov.com/Home/sergey-s-blog/intelgalileo-programminggpiofromlinux) for Intel Galileo GPIO mapping details.
*I will provide the script to enable Gen2 GPIO later*

```code
#!/bin/sh

#GPIO42 is IO10_MUX
echo -n "42" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio42/direction
echo -n "1" > /sys/class/gpio/gpio7/value

#initialize CS pin and drive the pin low.
echo -n "16" > /sys/class/gpio/export
echo -n "out" > /sys/class/gpio/gpio16/direction
echo -n "0" > /sys/class/gpio/gpio16/value

#SPI1 pins

#IO11_MUX (SPI1_MOSI)

echo "43" > /sys/class/gpio/export
echo "out" > /sys/class/gpio/gpio43/direction
echo "0" > /sys/class/gpio/gpio43/value

#IO12_MUX (SPI1_MISO)

echo "54" > /sys/class/gpio/export
echo "out" > /sys/class/gpio/gpio54/direction
echo "0" > /sys/class/gpio/gpio54/value

#IO13_MUX (SPI1_SCK)

echo "55" > /sys/class/gpio/export
echo "out" > /sys/class/gpio/gpio55/direction
echo "0" > /sys/class/gpio/gpio55/value



#note: TFT_DC pin (GPIO19) and SPI1 will be initialized & owned by the fbtft driver

```

Loading the Kernel Module from Yocto Shell:
===========================================

1. Execute the following shell commands in the shell to load fbtft driver:
```
~# modprobe fbtft_device custom name=fb_ili9341 buswidth=8 gpios=dc:19 width=240 height=320 busnum=1 bgr=1 speed=5000000
~# modprobe fb_ili9341
```
2. Verify that /dev/fb0 exists and execute "cat /dev/urandom > /dev/fb0" in the priviledged shell.
3. You should see random dots printed on the tft device.