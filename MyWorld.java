import greenfoot.*;
import java.util.ArrayList;

public class MyWorld extends World
{
    private static final int CELL_SIZE = 30;
    private static final int GAME_WIDTH = 21;   // has to be odd 
    private static final int GAME_HEIGHT = 21;  // has to be odd
    private static final int GAME_SPEED = 32;
    
    private static final int SCORE_POS_X = 5;
    private static final int SCORE_POS_Y = 25;
    
    private static final Font SCORE_FONT = new Font(true, false, 25);
    private static final Font GAME_OVER_MSG_FONT = new Font(true, false, 80);
    private static final Font RESTART_MSG_FONT = new Font(false, false, 40);
    
    private Snake snake;
    private Food food;
    private ArrayList<Body> body;
    private String direction;
    private String gameState;
    private boolean growing;
    private int score;
    
    private Label scoreLabel;
    private Label gameOverLabel;
    private Label restartLabel;
    
    public MyWorld()
    {    
        super(GAME_WIDTH, GAME_HEIGHT, CELL_SIZE);
        Greenfoot.setSpeed(GAME_SPEED);
        setPaintOrder(Label.class, Snake.class, Body.class, Food.class, MyWorld.class);
        setupWorld();
    }
    
    public void setupWorld()
    {
        removeObjects(getObjects(Label.class));
        removeObjects(getObjects(Snake.class));
        removeObjects(getObjects(Body.class));
        removeObjects(getObjects(Food.class));
        
        snake = new Snake();
        food = new Food();
        body = new ArrayList<Body>();
        
        direction = "UP";
        gameState = "playing";
        growing = false;
        score = 0;
                
        scoreLabel = new Label(this, SCORE_FONT);
        gameOverLabel = new Label(this, GAME_OVER_MSG_FONT);
        restartLabel = new Label(this, RESTART_MSG_FONT);
        
        scoreLabel.setString("score: " + score, SCORE_POS_X, SCORE_POS_Y);
        gameOverLabel.setString("Game over!", 100, 320);
        restartLabel.setString("press 'SPACE' to restart", 105, 360);
        
        addObject(snake, getWidth() / 2, getHeight() / 2);
        addObject(scoreLabel, 0, 0);
        spawnFood();
    }
    
    public void act()
    {
        if (gameState == "playing")
        {
            updateDirection();
            updateSnake();
            updateFood();
            scoreLabel.setString("score: "+score, SCORE_POS_X, SCORE_POS_Y);
        }
            
        if (gameState == "dead")
        {
            addObject(gameOverLabel, 0, 0);
            addObject(restartLabel, 0, 0);
            gameState = "game_over";
        }
            
        if (gameState == "game_over")
        {
            if (Greenfoot.isKeyDown("space"))
            {
                removeObject(gameOverLabel);
                removeObject(restartLabel);
                setupWorld();
            }
        }
            
    }
    
    public void updateDirection()
    {
        if ((Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up")) && direction != "DOWN")
        {
            direction = "UP";
        }
        else if ((Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left")) && direction != "RIGHT")
        {
            direction = "LEFT";
        }
        else if ((Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("down")) && direction != "UP")
        {
            direction = "DOWN";
        }
        else if ((Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right")) && direction != "LEFT")
        {
            direction = "RIGHT";
        }
    }
    
    public void updateSnake()
    {
        int posX = snake.getX();
        int posY = snake.getY();
        int vectX = direction == "LEFT" ? -1 : direction == "RIGHT" ? 1 : 0;
        int vectY = direction == "UP" ? -1 : direction == "DOWN" ? 1 : 0;
        
        if (direction == "UP" && posY == 0 || direction == "LEFT" && posX == 0 || direction == "DOWN" && posY == GAME_HEIGHT - 1 || direction == "RIGHT" && posX == GAME_WIDTH - 1)
        {
            gameState = "dead";
            return;
        }
        if (!getObjectsAt(posX + vectX, posY + vectY, Body.class).isEmpty())
        {
            if (growing || getObjectsAt(posX + vectX, posY + vectY, Body.class).get(0) != body.get(0))
            {
                gameState = "dead";
                return;
            }
        }
        
        snake.setLocation(posX + vectX, posY + vectY);
        
        if (growing)
        {
            growing = false;
        }
        else if (!body.isEmpty())
        {
            removeObject(body.get(0));
            body.remove(0);
        }
        body.add(new Body());
        addObject(body.get(body.size() - 1), posX, posY);
    }
    
    public void updateFood()
    {
        if (food.getX() == snake.getX() && food.getY() == snake.getY())
        {
            growing = true;
            removeObject(food);
            spawnFood();
            score++;
        }
    }
    
    public void spawnFood()
    {
        food = new Food();
        int pos = Greenfoot.getRandomNumber(GAME_WIDTH * GAME_HEIGHT - 1);
        while (!getObjectsAt(pos % GAME_WIDTH, pos / GAME_WIDTH, Body.class).isEmpty() || !getObjectsAt(pos % GAME_WIDTH, pos / GAME_WIDTH, Snake.class).isEmpty())
        {
            pos = (pos + 1) % (GAME_WIDTH * GAME_HEIGHT);
        }
        addObject(food, pos % GAME_WIDTH, pos / GAME_WIDTH);
    }
}
