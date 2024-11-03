package p1.fish.aquarium;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Aquarium extends Frame implements Runnable {

  private static final String IMAGE_SOURCE_PATH = "src/p1/fish/aquarium/img";

  private final MediaTracker tracker;
  private Image aquariumBackground, offScreenImage;
  private Graphics offScreenGraphics;
  private final Image[] fishImages = new Image[2];
  private final List<Fish> fishes = new ArrayList<>();
  private boolean running = true;

  public Aquarium() {
    setupWindow();
    tracker = new MediaTracker(this);
    loadResources();
    initializeFishes();
    startAnimation();
  }

  private void setupWindow() {
    setTitle("The Aquarium");
    setResizable(false);
    setVisible(true);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        running = false;
        System.exit(0);
      }
    });
  }

  private void loadResources() {
    fishImages[0] = loadImage("/fish2.png");
    fishImages[1] = loadImage("/fish1.png");
    aquariumBackground = loadImage("/bubbles.png");

    try {
      tracker.waitForAll();
    } catch (InterruptedException e) {
      System.err.println("Error loading images: " + e.getMessage());
    }

    setSize(aquariumBackground.getWidth(this), aquariumBackground.getHeight(this));
    offScreenImage = createImage(getWidth(), getHeight());
    offScreenGraphics = offScreenImage.getGraphics();
  }

  private Image loadImage(String path) {
    Image img = Toolkit.getDefaultToolkit().getImage(IMAGE_SOURCE_PATH + path);
    tracker.addImage(img, 0);
    return img;
  }

  private void initializeFishes() {
    Rectangle tankBounds = new Rectangle(0, 0, getWidth(), getHeight());
    int numberOfFish = 10;
    for (int i = 0; i < numberOfFish; i++) {
      fishes.add(new Fish(fishImages[0], fishImages[1], tankBounds, this));
    }
  }

  private void startAnimation() {
    Thread animationThread = new Thread(this);
    animationThread.start();
  }

  @Override
  public void paint(Graphics g) {
    if (offScreenGraphics != null) {
      offScreenGraphics.drawImage(aquariumBackground, 0, 0, this);
      for (Fish fish : fishes) {
        fish.swim();
        fish.drawFishImage(offScreenGraphics);
      }
      g.drawImage(offScreenImage, 0, 0, this);
    }
  }

  @Override
  public void update(Graphics g) {
    paint(g);
  }

  @Override
  public void run() {
    while (running) {
      repaint();
      try {
        int sleepTime = 110;
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        System.err.println("Animation interrupted: " + e.getMessage());
      }
    }
  }

  public static void main(String[] args) {
    new Aquarium();
  }
}