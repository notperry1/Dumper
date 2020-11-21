package cat.yoink.dumper;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Mod(modid = "dumper")
public class Main
{
    private final Logger logger = LogManager.getLogger("Dumper");

    @SuppressWarnings("unchecked")
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("Started");

        try
        {
            Field field = LaunchClassLoader.class.getDeclaredField("resourceCache");
            field.setAccessible(true);

            Map<String, byte[]> cache = (Map<String, byte[]>) field.get(Launch.classLoader);

            File file = new File(System.getenv("USERPROFILE") + "\\Desktop\\dump.jar");
            ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(file));

            for (Map.Entry<String, byte[]> e : cache.entrySet())
            {
                ZipEntry entry = new ZipEntry(e.getKey().replace(".", "/") + ".class");
                try { stream.putNextEntry(entry);

                stream.write(e.getValue());
                stream.closeEntry(); } catch (Exception ignored) { }
            }

            stream.closeEntry();

            logger.info("Ended");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
