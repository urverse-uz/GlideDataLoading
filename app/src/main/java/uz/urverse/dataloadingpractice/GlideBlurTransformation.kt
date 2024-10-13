package uz.urverse.dataloadingpractice

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class GlideBlurTransformation(private val context: Context) : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("blur_transformation".toByteArray())
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return blurBitmap(context, toTransform, 20f)
    }

    private fun blurBitmap(context: Context, bitmap: Bitmap, radius: Float): Bitmap {
        val output = Bitmap.createBitmap(bitmap)
        val renderScript = RenderScript.create(context)

        val input = Allocation.createFromBitmap(renderScript, bitmap)
        val outputAlloc = Allocation.createFromBitmap(renderScript, output)

        val blur = ScriptIntrinsicBlur.create(renderScript, input.element)
        blur.setRadius(radius)
        blur.setInput(input)
        blur.forEach(outputAlloc)

        outputAlloc.copyTo(output)
        renderScript.destroy()

        return output
    }
}