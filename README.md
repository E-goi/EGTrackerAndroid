# EGTrackerAndroid

## Usage

This Library allows the user to track events from his Android Apps to the E-Goi server.

## Requirements

## Installation

EGTrackerAndroid is available through. To install it, just add this to your gradle file:

```ruby
compile 'com.egoi.egtracker:0.1.0'
```

To use just configure the parameters and start tracking events:

```
    // Init the framework
    EGTracker.sharedInstance().initEngine(MainActivity.this);
    EGTracker.sharedInstance().configurations.url = "http://myappname.android";
    EGTracker.sharedInstance().configurations.clientID = 12324;
    EGTracker.sharedInstance().configurations.listID = 3214;
    EGTracker.sharedInstance().configurations.idsite = 1111;
    EGTracker.sharedInstance().configurations.subscriber = "mysubscriber@email.com";

    // Track app open
    EGTracker.sharedInstance().trackEvent("APP_OPEN");
```

## Author

Miguel Chaves, mchaves.apps@gmail.com

## License

Copyright (c) 2016 Miguel Chaves <mchaves.apps@gmail.com>

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
