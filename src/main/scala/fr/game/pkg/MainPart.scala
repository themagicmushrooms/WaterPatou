package fr.game.pkg

import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.{Quaternion, Vector3}

class MainPart(physics: Physics, size: Float, color: Color) extends CubePart(physics, size, color) {

  override def update() = {
    super.update()
    if (physics.isUnderwater(body))
      applyTurbineForces
  }

  private def applyTurbineForces = {
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      longitudinalPush(10f)
    } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      longitudinalPush(-5f)
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      spin(1f)
    } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      spin(-1f)
    }
    if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)) {
      verticalPush(10f)
    } else if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)) {
      verticalPush(-10f)
    }
  }

  private def longitudinalPush(force: Float) = {
    val intensity = mass * force
    val dir = new Vector3(1f, 0f, 0f)
    val q = new Quaternion
    body.getWorldTransform().getRotation(q)
    dir.mul(q).nor()
    body.applyCentralForce(dir.scl(intensity))
  }

  private def verticalPush(force: Float) = {
    val intensity = mass * force
    val dir = new Vector3(0f, 0f, 1f)
    body.applyCentralForce(dir.scl(intensity))
  }

  private def spin(force: Float) = {
    val intensity = mass * force
    val dir = new Vector3(0f, 0f, 1f)
    body.applyTorque(dir.scl(intensity))
  }

  override def calculateInertia: Vector3 = {
    val i = super.calculateInertia
    i.x = 0f
    i.y = 0f
    i
  }

  override def density: Float = physics.heavyWoodDensity

}
