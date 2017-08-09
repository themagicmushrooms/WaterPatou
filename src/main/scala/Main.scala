package fr.game.pkg

import com.badlogic.gdx.backends.lwjgl._
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.{Game, Gdx}
import com.badlogic.gdx.graphics.{Color, GL20, PerspectiveCamera, Texture}
import com.badlogic.gdx.graphics.g3d.attributes.{BlendingAttribute, ColorAttribute}
import com.badlogic.gdx.graphics.g3d._
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.{CameraInputController, ModelBuilder}
import com.badlogic.gdx.math.{Plane, Vector3}
import com.badlogic.gdx.physics.bullet.collision._
import com.badlogic.gdx.physics.bullet.dynamics._

class MyGame extends Game {

  var modelBatch: ModelBatch = null
  var cam: PerspectiveCamera = null
  var camControl: CameraInputController = null
  var envir: Environment = null

  var partModel: Model = null
  var partModelInstance: ModelInstance = null
  var partShape: btCollisionShape = null
  var partBody: btRigidBody = null

  var physics: Physics = null

  var waterModel: Model = null
  var waterInstance: ModelInstance = null

  override def create() = {
    physics = new Physics
    modelBatch = new ModelBatch
    cam = createCam

    val builder = new ModelBuilder
    partModel = builder.createBox(5f, 5f, 5f,
      new Material(ColorAttribute.createDiffuse(Color.GREEN)),
      Usage.Position | Usage.Normal)
    partModelInstance = new ModelInstance(partModel)
    partModelInstance.transform.translate(0f, 12f, 0f)
    partShape = new btBoxShape(new Vector3(2.5f, 2.5f, 2.5f))
    val ci = new btRigidBody.btRigidBodyConstructionInfo(125f, null, partShape)
    partBody = new btRigidBody(ci)
    partBody.setCollisionShape(partShape)
    partBody.setWorldTransform(partModelInstance.transform)
    physics.add(partBody)

    waterModel = builder.createRect (
      -1f, 0f, -1f,
      -1f, 0f, 1f,
      1f, 0f, 1f,
      1f, 0f, -1f,
      0f, 1f, 0f,
      new Material(
        ColorAttribute.createDiffuse(Color.BLUE),
        new BlendingAttribute(true, 0.6f)),
      Usage.Position | Usage.Normal)
    waterInstance = new ModelInstance(waterModel)
    waterInstance.transform.scale(100f, 100f, 100f)
    envir = new Environment
    envir.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
    envir.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    camControl = new CameraInputController(cam)
    Gdx.input.setInputProcessor(camControl)
  }

  private def createCam = {
    val cam = new PerspectiveCamera(90, Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    cam.position.set(10f, 10f, 10f)
    cam.lookAt(0f, 0f, 0f)
    cam.near = 1f
    cam.far = 300f
    cam.update()
    cam
  }

  override def render() = {

    val delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime)
    physics.step(delta)

    partBody.getWorldTransform(partModelInstance.transform)

    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT)
    modelBatch.begin(cam)
    modelBatch.render(partModelInstance, envir)
    modelBatch.render(waterInstance, envir)
    modelBatch.end()
    camControl.update()
  }

  override def dispose() = {
    modelBatch.dispose()
    partModel.dispose()
  }

}

object Main extends App {
  val cfg = new LwjglApplicationConfiguration
  cfg.title = "My Game"
  cfg.height = 480
  cfg.width = 800
  cfg.forceExit = false
  new LwjglApplication(new MyGame, cfg)
}
