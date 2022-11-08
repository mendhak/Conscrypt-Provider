Conscrypt Provider is an APK which can provide the Conscrypt Library to apps that support older Android devices. 
The [Conscrypt Library](https://github.com/google/conscrypt) provides modern TLS capabilities and ciphers, including TLS 1.3.   
I have wrapped it in a standalone APK because not all users will need it, and because the library is quite large.

Users will need to install the APK, and app developers will need to add code to their apps to make use of this provider. 

## Instructions for users

Download the app from [F-Droid](https://f-droid.org/packages/com.mendhak.conscryptprovider/) or go to the [releases](https://github.com/mendhak/Conscrypt-Provider/releases).  Download the `.apk` file and install it. 

(Optional) It's always a good idea to verify downloads (the method below only works with apk from the release section). First get my PGP public key

    gpg --recv-key 6989CF77490369CFFDCBCD8995E7D75C76CBE9A9

You can verify the APK signature using:

    gpg --verify ~/Downloads/conscrypt-provider-1.apk.asc

You can verify the APK checksum using:

    sha256sum -c ~/Downloads/conscrypt-provider-1.apk.SHA256

## Instructions for developers

In the app startup code, you can look for the APK being installed, and if it is, include it.   

First, get your helper methods ready

```java
public static String getPackageSignature(String targetPackage, Context context) throws PackageManager.NameNotFoundException, CertificateException, NoSuchAlgorithmException {
    Signature sig = context.getPackageManager().getPackageInfo(targetPackage, PackageManager.GET_SIGNATURES).signatures[0];
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(sig.toByteArray()));
    String hexString = null;
    MessageDigest md = MessageDigest.getInstance("SHA1");
    byte[] publicKey = md.digest(cert.getEncoded());
    hexString = byte2HexFormatted(publicKey);
    return hexString;
}

static String byte2HexFormatted(byte[] arr) {
    StringBuilder str = new StringBuilder(arr.length * 2);
    for (int i = 0; i < arr.length; i++) {
        String h = Integer.toHexString(arr[i]);
        int l = h.length();
        if (l == 1) h = "0" + h;
        if (l > 2) h = h.substring(l - 2, l);
        str.append(h.toUpperCase());
        if (i < (arr.length - 1)) str.append(':');
    }
    return str.toString();
}
```

Then early in the application lifecycle, do this: 

```java
// You should probably check if com.mendhak.conscryptprovider is installed first. 
// https://stackoverflow.com/q/6758841/974369
// Then:
try {
    //Get signature to compare - either Github or F-Droid versions
    //~/Android/Sdk/build-tools/33.0.0/apksigner verify --print-certs -v ~/Downloads/com.mendhak.conscryptprovider_3.apk
    String signature = getPackageSignature("com.mendhak.conscryptprovider", context);
    if (
            signature.equalsIgnoreCase("C7:90:8D:17:33:76:1D:F3:CD:EB:56:67:16:C8:00:B5:AF:C5:57:DB")
            || signature.equalsIgnoreCase("9D:E1:4D:DA:20:F0:5A:58:01:BE:23:CC:53:34:14:11:48:76:B7:5E")
    ) {
        signatureMatch = true;
    }
    else {
        Log.e("com.mendhak.conscryptprovider found, but with an invalid signature. Ignoring.");
        return;
    }

    //https://gist.github.com/ByteHamster/f488f9993eeb6679c2b5f0180615d518
    Context targetContext = context.createPackageContext("com.mendhak.conscryptprovider",
            Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
    ClassLoader classLoader = targetContext.getClassLoader();
    Class installClass = classLoader.loadClass("com.mendhak.conscryptprovider.ConscryptProvider");
    Method installMethod = installClass.getMethod("install", new Class[]{});
    installMethod.invoke(null);
    installed = true;
    Log.i("Conscrypt Provider installed");
} catch (Exception e) {
    Log.e("Could not install Conscrypt Provider", e);
}

```

Of course within the app, you'll also need to provide instructions for users to install the APK.

## Motivation

I want to provide TLS 1.3 to pre-Android-10 users in my app, without having to rely on closed source libraries.  
The simplest way to do this is to include the [Conscrypt library](https://github.com/google/conscrypt/). 

However, this library is massive, it adds about 5+MB to the APK size.  
And it isn't needed for [Android 10+ users](https://developer.android.com/about/versions/10/features#tls-1.3).  

The next simplest way to deal with this situation is to make the Conscrypt provider a separate app, and reference it from my application. 

This repository contains the code for the 'Conscrypt Provider'.  
It can probably be used by any application though.  


## References

I have made use of the [F-Droid blog post](https://f-droid.org/2020/05/29/android-updates-and-tls-connections.html) and an [associated gist](https://gist.github.com/ByteHamster/f488f9993eeb6679c2b5f0180615d518).

This isn't the best or perfect way to provide Conscrypt to applications, but it's _a_ way that works for me.  

App Icon by [Flaticon.com](https://www.flaticon.com/free-icon/tls-protocol_4896619?term=tls&page=1&position=2&page=1&position=2&related_id=4896619&origin=style)
