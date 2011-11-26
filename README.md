# ZipDownloader #

This is an example app for Android that shows how to implement a background task that will download a .zip file from an internet URL, then unzip the contents to a cache folder on external storage (a.k.a the SD card).

Often applications, such as games, which have a lot of image and audio assets need to perform a one-time download at first run of the application.  This is because Google enforces a 50-megabyte limit for Android APK downloads.  Additionally, some other use cases require downloading portions of an app immediately after installation.

## About the code ##

### Classes ###

#### `com.jotabout.zipdownloader` package ####

`MainActivity`  

 * Main application UI.  Also contains background task for download.

#### `com.jotabout.zipdownloader.util` package ####

`DecompressZip`  

 * High-level wrapper to easily unzip files.

`DownloadFile`  

 * Utility methods to download a file from an internet URL.

`ExternalStorage`  

 * A wrapper class that answers the question, "Give me a place to put my stuff."

### Developer notes ###

The code:

* Traps errors and cleans up after itself in the event of error
* Performs all I/O operatons (network and filesystem) in the background, for StrictMode compliance
* Keeps the screen alive while the download is in process
* Locates the files on external storage in a place which will be automatically deleted by Android on uninstall
* Uses buffered I/O for efficiency
* Manages internal data in a way as to (mostly) be friendly to the Dalvik garbage collector.

In short, it is production-ready code.

## License ##

Copyright (c) 2011 Michael J. Portuesi (http://www.jotabout.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
