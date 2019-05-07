package com.obstacleavoid.v2.desktop

import com.badlogic.gdx.tools.texturepacker.TexturePacker

object AssetPacker {

    const val DRAW_DEBUG_OUTLINE = false
    const val RAW_ASSETS_PATH = "desktop/assets-raw/gameplay"
    const val ASSETS_PATH = "android/assets/gameplay"
}

fun main(args: Array<String>) {
    val settings = TexturePacker.Settings().apply {
        debug = AssetPacker.DRAW_DEBUG_OUTLINE
        maxWidth = 2048
        maxHeight = 2048
    }

    TexturePacker.process(settings,
            "${AssetPacker.RAW_ASSETS_PATH}",
            "${AssetPacker.ASSETS_PATH}",
            "gameplay")
}