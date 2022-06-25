Conscrypt Provider is an APK which can provide the Conscrypt Library to apps that support older Android devices. 
The [Conscrypt Library](https://github.com/google/conscrypt) provides modern TLS capabilities and ciphers, including TLS 1.3.   
It is provided as a separate APK because not all users will need it, and because the library is quite large. 

## Motivation

I want to provide TLS 1.3 to pre-Android-10 users in my app, without having to rely on closed source libraries.  
The simplest way to do this is to include the [Conscrypt library](https://github.com/google/conscrypt/). 

However, this library is massive, it adds about 5+MB to the APK size.  
And it isn't needed for [Android 10+ users](https://developer.android.com/about/versions/10/features#tls-1.3).  

The next simplest way to deal with this situation is to make the Conscrypt provider a separate app, and reference it from my application. 

This repository contains the code for the 'Conscrypt Provider'.  
It can probably be used by any application though.  


## Instructions

### Download

Go to the [releases](https://github.com/mendhak/Conscrypt-Provider/releases).  Download the `.apk` file. 

It's always a good idea to verify downloads.  First get my PGP public key

    gpg --recv-key 6989CF77490369CFFDCBCD8995E7D75C76CBE9A9

You can verify the signature using:

    gpg --verify ~/Downloads/conscrypt-provider-1.apk.asc

You can verify the checksum using:

    sha256sum -c ~/Downloads/conscrypt-provider-1.apk.SHA256


### Calling from your application

TBC: I'll add code here on how to reference it from a calling application. 

```java
    Context targetContext = context.createPackageContext("com.mendhak.conscryptprovider",
            Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
    ClassLoader classLoader = targetContext.getClassLoader();
    Class installClass = classLoader.loadClass("com.mendhak.conscryptprovider.ConscryptProvider");
    Method installMethod = installClass.getMethod("install", new Class[]{});
    installMethod.invoke(null);
    installed = true;
```



## References

I have made use of the [F-Droid blog post](https://f-droid.org/2020/05/29/android-updates-and-tls-connections.html) and an [associated gist](https://gist.github.com/ByteHamster/f488f9993eeb6679c2b5f0180615d518).

This isn't the best or perfect way to provide Conscrypt to applications, but it's _a_ way that works for me.  

App Icon by [Flaticon.com](https://www.flaticon.com/free-icon/tls-protocol_4896619?term=tls&page=1&position=2&page=1&position=2&related_id=4896619&origin=style)