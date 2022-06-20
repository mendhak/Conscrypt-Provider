
## Motivation

I want to provide TLS 1.3 to pre-Android-10 users in my app.  The simplest way to do this is to include the [Conscrypt library](https://github.com/google/conscrypt/). 

However, this library is massive, it adds about 5+MB to the APK size.  And it isn't needed for [Android 10+ users](https://developer.android.com/about/versions/10/features#tls-1.3).  

The next simplest way to deal with this situation is to make the Conscrypt provider a separate app, and reference it from my application. 

This repository contains the code for the 'Conscrypt Provider'.  It can probably be used by any application though.  


## Instructions

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

