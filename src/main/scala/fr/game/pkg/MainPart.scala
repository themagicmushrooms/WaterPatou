package fr.game.pkg

import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.graphics.{Color, GL20}
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.{BoxShapeBuilder, CapsuleShapeBuilder, SphereShapeBuilder}
import com.badlogic.gdx.graphics.g3d.{Material, ModelInstance}
import com.badlogic.gdx.math.{Quaternion, Vector3}

class MainPart(physics: Physics, size: Float, color: Color) extends CubePart(physics, size, color) {

  builder.begin
  val mainNode = builder.node()
  BoxShapeBuilder.build(
    builder.part("main", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(color))),
    size, size, size)
  val left = builder.node()
  left.rotation.set(new Vector3(1f, 0f, 0f), 80f)
  left.translation.set(size * 0.25f, size * 0.25f, size / 2)
  CapsuleShapeBuilder.build(
    builder.part("left", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(color))),
    size / 6, size, 15)
  val right = builder.node()
  right.rotation.set(new Vector3(-1f, 0f, 0f), 80f)
  right.translation.set(size * 0.25f, -size * 0.25f, size / 2)
  CapsuleShapeBuilder.build(
    builder.part("right", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(color))),
    size / 6, size, 15)
  model = builder.end
  instance = new ModelInstance(model)

  override def update() = {
    super.update()
    if (physics.isUnderwater(body))
      applyTurbineForces
  }

  private def applyTurbineForces = {
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      longitudinalPush(20f)
    } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      longitudinalPush(-10f)
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      spin(10f)
    } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      spin(-10f)
    }
    if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)) {
      verticalPush(20f)
    } else if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)) {
      verticalPush(-20f)
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
