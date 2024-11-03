package p1.fish.aquarium;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class Fish {

  private final Component tank;
  private final Image imageLeft;
  private final Image imageRight;
  private final Rectangle boundaries;
  private final Random random = new Random();
  private final Point velocity;
  private final Point location;
  private static final int MAX_VELOCITY = 6;
  private static final int MIN_VELOCITY = 2;

  public Fish(Image imageLeft, Image imageRight, Rectangle boundaries, Component tank) {
    this.tank = tank;
    this.imageLeft = imageLeft;
    this.imageRight = imageRight;
    this.boundaries = boundaries;

    // Initialize location within boundaries
    this.location = new Point(
        200 + random.nextInt(200),
        100 + random.nextInt(150)
    );

    int initialXDirection = random.nextBoolean() ? 1 : -1;
    int initialYDirection = random.nextBoolean() ? 1 : -1;

    this.velocity = new Point(
        initialXDirection * (MIN_VELOCITY + random.nextInt(MAX_VELOCITY - MIN_VELOCITY)),
        initialYDirection * (MIN_VELOCITY + random.nextInt(MAX_VELOCITY - MIN_VELOCITY))
    );
  }

  public void swim() {
    updateVelocity();
    updateLocation();
    handleBoundaries();
  }

  private void updateVelocity() {
    if (random.nextInt(100) < 5) {
      velocity.x = Math.max(-MAX_VELOCITY, Math.min(MAX_VELOCITY, velocity.x + random.nextInt(3) - 1));
      velocity.y = Math.max(-MAX_VELOCITY, Math.min(MAX_VELOCITY, velocity.y + random.nextInt(3) - 1));

      if (Math.abs(velocity.x) < MIN_VELOCITY) {
        velocity.x += (velocity.x < 0) ? -1 : 1;
      }
      if (Math.abs(velocity.y) < MIN_VELOCITY) {
        velocity.y += (velocity.y < 0) ? -1 : 1;
      }
    }
  }

  private void updateLocation() {
    location.x += velocity.x;
    location.y += velocity.y;
  }

  private void handleBoundaries() {
    if (location.x < boundaries.x) {
      location.x = boundaries.x;
      velocity.x = Math.abs(velocity.x);
    } else if (location.x + imageLeft.getWidth(tank) > boundaries.width) {
      location.x = boundaries.width - imageLeft.getWidth(tank);
      velocity.x = -Math.abs(velocity.x);
    }

    if (location.y < boundaries.y) {
      location.y = boundaries.y;
      velocity.y = Math.abs(velocity.y);
    } else if (location.y + imageLeft.getHeight(tank) > boundaries.height) {
      location.y = boundaries.height - imageLeft.getHeight(tank);
      velocity.y = -Math.abs(velocity.y);
    }
  }

  public void drawFishImage(Graphics g) {
    g.drawImage(velocity.x < 0 ? imageLeft : imageRight, location.x, location.y, tank);
  }

}