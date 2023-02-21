import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import javax.security.auth.login.LoginException;

public class Main{
    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault("token")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES)
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(Activity.listening("!help"))
                .addEventListeners(new Manager())
                .build();
    }
}
