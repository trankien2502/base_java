//    public void loadNativeAds() {
//        try {
//            if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsNativeHome.size() != 0 && ConstantRemote.native_home) {
//                @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_native_load_large_home, null);
//                binding.nativeHome.removeAllViews();
//                binding.nativeHome.addView(adViewLoad);
//                binding.nativeHome.setVisibility(View.VISIBLE);
//                Admob.getInstance().loadNativeAd(this, ConstantIdAds.listIDAdsNativeHome, new AdCallback() {
//                    @Override
//                    public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
//                        @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_native_show_large_home, null);
//                        binding.nativeHome.removeAllViews();
//                        binding.nativeHome.addView(adView);
//                        Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
//                        binding.nativeHome.removeAllViews();
//                    }
//                });
//            } else {
//                binding.nativeHome.setVisibility(View.GONE);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            binding.nativeHome.removeAllViews();
//        }
//    }
//
//    private void loadInterAll() {
//        if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.mInterAll == null && ConstantIdAds.listIDAdsInterAll.size() != 0) {
//            ConstantIdAds.mInterAll = CommonAd.getInstance().getInterstitialAds(this, ConstantIdAds.listIDAdsInterAll);
//        }
//    }
//
//    public void showInterGhost() {
//        if (IsNetWork.haveNetworkConnection(this) && !ConstantIdAds.listIDAdsInterAll.isEmpty() && ConstantRemote.inter_option) {
//            if (System.currentTimeMillis() - ConstantRemote.interval_interstitial_from_start > ConstantRemote.interval_interstitial_from_start_old * 1000) {
//                if (System.currentTimeMillis() - ConstantRemote.time_interval_old > ConstantRemote.interval_between_interstitial * 1000) {
//                    try {
//                        if (ConstantIdAds.mInterAll != null) {
//                            CommonAd.getInstance().forceShowInterstitialByTime(this, ConstantIdAds.mInterAll, new CommonAdCallback() {
//                                @Override
//                                public void onAdClosed() {
//                                    super.onAdClosed();
//                                    resultLauncher.launch(new Intent(HomeActivity.this, OptionActivity.class));
//                                }
//
//                                @Override
//                                public void onAdClosedByTime() {
//                                    super.onAdClosedByTime();
//                                    ConstantRemote.time_interval_old = System.currentTimeMillis();
//                                    ConstantIdAds.mInterAll = null;
//                                    loadInterAll();
//                                }
//                            }, true);
//                        } else {
//                            resultLauncher.launch(new Intent(this, OptionActivity.class));
//                            ConstantIdAds.mInterAll = null;
//                            loadInterAll();
//                        }
//                    } catch (Exception e) {
//                        resultLauncher.launch(new Intent(this, OptionActivity.class));
//                    }
//                } else {
//                    resultLauncher.launch(new Intent(this, OptionActivity.class));
//                }
//            } else {
//                resultLauncher.launch(new Intent(this, OptionActivity.class));
//            }
//        } else {
//            resultLauncher.launch(new Intent(this, OptionActivity.class));
//        }
//    }
//    private void loadBannerCollapsible() {
//        if (IsNetWork.haveNetworkConnection(this) && ConstantIdAds.listIDAdsBannerCollapsible.size() != 0 && ConstantRemote.banner_collapsible_ghostscanning) {
//            CommonAd.getInstance().loadCollapsibleBannerFloor(this, ConstantIdAds.listIDAdsBannerCollapsible, "bottom");
//            findViewById(R.id.banner).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.banner).setVisibility(View.GONE);
//        }
//    }
//    private void loadBanner() {
//        if (IsNetWork.haveNetworkConnectionUMP(SplashActivity.this) && ConstantIdAds.listIDAdsBanner.size() != 0 && ConstantRemote.banner_splash) {
//            binding.rlBanner.setVisibility(View.VISIBLE);
//            Admob.getInstance().loadBannerFloor(SplashActivity.this, ConstantIdAds.listIDAdsBanner);
//        } else {
//            binding.rlBanner.setVisibility(View.GONE);
//        }
//    }
