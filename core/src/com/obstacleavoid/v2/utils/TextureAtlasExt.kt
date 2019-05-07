package com.obstacleavoid.v2.utils

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

inline operator fun TextureAtlas.get(regionName: String) : TextureRegion? {
    return findRegion(regionName)
}