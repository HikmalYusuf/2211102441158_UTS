import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Label extends Actor
{   
    public Label(MyWorld world, Font font)
    {
        GreenfootImage img = new GreenfootImage(world.getWidth() * world.getCellSize(), world.getHeight() * world.getCellSize());
        img.setFont(font);
        setImage(img);
    }
    
    protected void addedToWorld(World world)
    {
        setLocation(world.getWidth() / 2, world.getHeight() / 2);
    }
    
    public void setString(String text, int x, int y)
    {
        getImage().clear();
        getImage().drawString(text, x, y);
    }
}
