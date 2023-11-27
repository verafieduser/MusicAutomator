using Soulseek;

public class SoulseekClientWrapper
{
    static public void Main ()
    {
        var Try = new SoulseekClientWrapper();
        Try.Test();

        
    }

    async void Test(){
        var client = new SoulseekClient();
        await client.ConnectAsync("Username", "Password");
        
        Console.WriteLine(client.ToString());
    }

}
