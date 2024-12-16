Pod::Spec.new do |spec|
    spec.name                     = 'Shared'
    spec.version                  = '1.0'
    spec.homepage                 = 'https://github.com/ahmadAlfhajri/kmp-minimalist-template_test_script'
    spec.source                   = { :http=> 'https://github.com/ahmadAlfhajri/kmp-minimalist-template_test_script/releases/download/1.0.0/XenditFingerprintSDK-1.0.0.zip'}
    spec.authors                  = ''
    spec.license                  = { :type => 'MIT', :text => 'License text'}
    spec.summary                  = 'Some description for the Shared Module'
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '15.0'
    spec.dependency 'XenditFingerprintSDK', '1.0.1'





end