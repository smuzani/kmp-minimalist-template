// swift-tools-version:5.9.0
import PackageDescription

let package = Package(
    name: "Shared",
    platforms: [
        .iOS(.v15),
    ],
    products: [
        .library(name: "Shared", targets: ["SharedSDKTarget"])
    ],
    targets: [
        .binaryTarget(
            name: "Shared",
            path: "https://github.com/ahmadAlfhajri/kmp-minimalist-template_test_script/releases/download/1.0.0/XenditFingerprintSDK-1.0.0.zip",
            checksum:"35ec66e6e0d792a5affa884f117842ac6ccfc9bf9908a07449eac7152995d23a"
        ),
        .binaryTarget(
            name: "XenditFingerprintSDK",
            url: "https://cdn-xenshield.xendit.co/fingerprint-sdk/ios/1.0.1/XenditFingerprintSDK-1.0.1.zip",
            checksum: "d8dbb2e00525eb7765972e10aa0cf49d990a7cc40ddff05d0f620a17b487ceb0"
        ),
        .target(
            name: "SharedSDKTarget",
            dependencies: [
                .target(name: "Shared"),
                .target(name: "XenditFingerprintSDK"),
//                 .product(name: "Reachability", package: "Reachability.swift")
            ],
            path:"SharedSDKTarget",
            linkerSettings: [
                    // Equivalent to s.framework = 'SystemConfiguration'
                    .linkedFramework("SystemConfiguration"),
                    // Equivalent to s.ios.framework = 'CoreTelephony'
                    .linkedFramework("CoreTelephony", .when(platforms: [.iOS]))
                ]
        )
    ]
)


//https://stackoverflow.com/questions/68663996/swift-package-manager-make-package-depend-on-remote-xcframework
//https://forums.swift.org/t/swiftpm-binary-target-with-sub-dependencies/40197/27
//https://github.com/amplitude/Amplitude-iOS/blob/main/Amplitude.podspec <- Example