// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.webviewflutter;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;

public class WebViewFlutterPlugin implements FlutterPlugin {

    private FlutterCookieManager flutterCookieManager;

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        // X5WebView初始化
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        QbSdk.initX5Environment(binding.getApplicationContext(), null);

        //
        BinaryMessenger messenger = binding.getBinaryMessenger();
        binding.getPlatformViewRegistry()
                .registerViewFactory("plugins.flutter.io/webview",
                        new WebViewFactory(messenger, /*containerView=*/ null)
                );
        flutterCookieManager = new FlutterCookieManager(messenger);
    }

    @Override
    public void onDetachedFromEngine(@NotNull FlutterPluginBinding binding) {
        if (flutterCookieManager == null) {
            return;
        }
        flutterCookieManager.dispose();
        flutterCookieManager = null;
    }
}
