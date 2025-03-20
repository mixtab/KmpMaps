package org.m_tabarkevych.kmpmaps.features.core.presentation

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.*

@OptIn(ExperimentalForeignApi::class)
actual fun showToast(text: String) {
       /* val keyWindow = UIApplication.sharedApplication.keyWindow
        if (keyWindow == null) return

        // Create a simple UILabel to show the toast
        val toastLabel = UILabel().apply {
            this.text = text
            font = UIFont.systemFontOfSize(16.0) // Adjust the font size as needed
            textColor = UIColor.whiteColor
            backgroundColor = UIColor.blackColor.colorWithAlphaComponent(0.8)
            textAlignment = NSTextAlignmentCenter
            layer.cornerRadius = 10.0
            layer.masksToBounds = true
        }

        // Set the size and position of the toast
        toastLabel.setFrame(CGRectMake(0.0, 0.0, 300.0, 50.0))
        toastLabel.center = keyWindow.anchorPoint


        // Add the toast label to the window
        keyWindow.addSubview(toastLabel)

        // Animate the toast to disappear after 3 seconds
        UIView.animateWithDuration(3.0, delay = 0.0, options = UIViewAnimationOptionCurveEaseOut, animations = {
            toastLabel.alpha = 0.0
        }) {
            toastLabel.removeFromSuperview()
        }*/
    }