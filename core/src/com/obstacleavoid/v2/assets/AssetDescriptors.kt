package com.obstacleavoid.v2.assets

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.obstacleavoid.v2.utils.assetDescriptor

object AssetDescriptors {

    val FONT = assetDescriptor<BitmapFont>(AssetPaths.SITKA_FONT)
    val GAMEPLAY_ATLAS = assetDescriptor<TextureAtlas>(AssetPaths.GAMEPLAY_ATLAS)
    val SKIN_ATLAS = assetDescriptor<TextureAtlas>(AssetPaths.SKIN_ATLAS)
}