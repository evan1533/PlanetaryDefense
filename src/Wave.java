public class Wave
{
	char TYPE;
	int LEVEL, SIZE, DEPLOY_TIME;
	boolean deployed = false;
	int lastDeployment;

	public Wave(char typ, int level, int size, int deployTime)
	{
		TYPE = typ;
		LEVEL = level;
		SIZE = size;
		DEPLOY_TIME = deployTime;
	}

	public void deploy()
	{
		this.deployed = true;
		lastDeployment = GameTimer.getSeconds();
	}

	public void tick()
	{
		lastDeployment++;
	}
}