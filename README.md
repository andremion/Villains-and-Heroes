[![License Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=true)](http://www.apache.org/licenses/LICENSE-2.0)
![minSdkVersion 14](https://img.shields.io/badge/minSdkVersion-14-red.svg?style=true)
![compileSdkVersion 25](https://img.shields.io/badge/compileSdkVersion-25-yellow.svg?style=true)
[![Build Status](https://travis-ci.org/andremion/Villains-and-Heroes.svg?branch=master)](https://travis-ci.org/andremion/Villains-and-Heroes)
[![codecov](https://codecov.io/gh/andremion/Villains-and-Heroes/branch/master/graph/badge.svg)](https://codecov.io/gh/andremion/Villains-and-Heroes)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Villains--&--Heroes-green.svg?style=true)](https://android-arsenal.com/details/3/4606)
[![Awesome Android #22](https://img.shields.io/badge/Awesome%20Android-%2322-green.svg?style=true)](https://android.libhunt.com/newsletter/22)
[![Awesome Android #36](https://img.shields.io/badge/Awesome%20Android-%2336-green.svg?style=true)](https://android.libhunt.com/newsletter/36)

![Icon](https://raw.githubusercontent.com/andremion/Villains-and-Heroes/master/app/src/main/res/mipmap-hdpi/ic_launcher.png)
# Villains & Heroes

Android app built with [MVP](http://antonioleiva.com/mvp-android/) architectural approach and uses [Marvel Comics API](https://developer.marvel.com) that allows developers everywhere to access information about Marvel's vast library of comics.

![Preview](https://raw.githubusercontent.com/andremion/Villains-and-Heroes/master/art/preview.gif)
![Widget](https://raw.githubusercontent.com/andremion/Villains-and-Heroes/master/art/widget-github.png)

[![Get it on Google Play](https://developer.android.com/images/brand/en_generic_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.andremion.heroes)

## Credentials

1. Put your secret Marvel keys in `marvelPrivateKey` and `marvelPublicKey` properties in your `gradle.properties` or set `MARVEL_PRIVATE_KEY` and `MARVEL_PUBLIC_KEY` variables in your Environment Variables

## References, libraries and tools used in the project 

* [Android Architecture Blueprints](https://github.com/googlesamples/android-architecture)
Demonstrate possible ways to help with testing, maintaining and extending of an Android app using different architectural concepts and tools.
* [Data Binding](https://developer.android.com/topic/libraries/data-binding)
Write declarative layouts and minimize the glue code necessary to bind application logic and layouts
* [Retrofit](http://square.github.io/retrofit)
A type-safe HTTP client for Android and Java
* [Gson](https://github.com/google/gson)
A Java serialization/deserialization library that can convert Java Objects into JSON and back.
* [Design Support Library](http://developer.android.com/intl/pt-br/tools/support-library/features.html#design)
The Design package provides APIs to support adding material design components and patterns to your apps.
* [RecyclerView](http://developer.android.com/intl/pt-br/reference/android/support/v7/widget/RecyclerView.html)
A flexible view for providing a limited window into a large data set.
* [Glide](https://github.com/bumptech/glide)
An image loading and caching library for Android focused on smooth scrolling
* [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/index.html)
Use Espresso to write concise, beautiful, and reliable Android UI tests

## License

    Copyright 2016 André Mion

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
