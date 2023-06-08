package io.github.sceneview.ar.node

import com.google.ar.core.Camera
import io.github.sceneview.ar.arcore.position
import io.github.sceneview.ar.arcore.quaternion
import io.github.sceneview.node.CameraNode
import kotlin.math.atan

/**
 * Represents a virtual camera, which determines the perspective through which the scene is viewed.
 *
 *
 * If the camera is part of an [ArSceneView], then the camera automatically tracks the
 * camera pose from ARCore. Additionally, the following methods will throw [ ] when called:
 *
 *
 *  * [.setParent] - CameraNode's parent cannot be changed, it is always the scene.
 *  * [.setPosition] - CameraNode's position cannot be changed, it is controlled
 * by the ARCore camera pose.
 *  * [.setRotation] - CameraNode's rotation cannot be changed, it is
 * controlled by the ARCore camera pose.
 *
 *
 *
 * All other functionality in Node is supported. You can access the position and rotation of the
 * camera, assign a collision shape to the camera, or add children to the camera. Disabling the
 * camera turns off rendering.
 */
class ArCameraNode : CameraNode(false) {

    override var verticalFovDegrees: Float
        get() {
            val fovRadians = 2.0 * atan(1.0 / projectionMatrix.data[5])
            return Math.toDegrees(fovRadians).toFloat()
        }
        set(_) {}

    /**
     * Updates the pose and projection of the camera to match the tracked pose from ARCore.
     *
     * Called internally as part of the integration with ARCore, should not be called directly.
     */
    fun updateTrackedPose(camera: Camera) {
        // Update the projection matrix.
        camera.getProjectionMatrix(projectionMatrix.data, 0, nearClipPlane, farClipPlane)

        // Update the view matrix.
        camera.getViewMatrix(viewMatrix.data, 0)

        // Update the node's transformation properties to match the tracked pose.
        val pose = camera.displayOrientedPose
        super.position = pose.position
        super.quaternion = pose.quaternion
    }
}