import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class WatcherWorker {
    private String sourceDir;
    private ArrayList<String> targets;
    private Logger logger;
    private WatchKey key;
    private Worker worker;

    public WatcherWorker(String sourceDir, ArrayList<String> targets){
        this.sourceDir = sourceDir;
        this.targets = targets;
    }

    public WatcherWorker(String sourceDir){
        this.sourceDir = sourceDir;
        this.targets = new ArrayList<String>();
    }

    public void preStart() throws DirectoryNotFoundException, IOException {
        if(!check())
            throw new DirectoryNotFoundException("No se han encontrado");
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path source = Paths.get(this.sourceDir);
        if(logger != null)
            logger.info("Iniciado el watcher", "WatcherWorker.start");
        worker = new Worker(source, watchService);
        worker.setLogger(logger);
        key = worker.getKey();
        worker.setTargets(targets);
    }

    public void start(){
        worker.start();
    }

    public boolean check(){
        boolean out = true;
        out = out && new File(sourceDir).isDirectory();
        for(String folder : targets){
            out = out && new File(folder).isDirectory();
        }
        return out;
    }

    public ArrayList<String> getTargets() {
        return targets;
    }

    public Worker getWorker() {
        return worker;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public WatchKey getKey() {
        return key;
    }

    public void setTargets(ArrayList<String> targets) {
        this.targets = targets;
    }

    public static final class DirectoryNotFoundException extends Exception{
        public DirectoryNotFoundException(String message) {
            super(message);
        }
    }

    public static final class Worker extends Thread{
        private Path source;
        private WatchKey key;
        private WatchService service;
        private Logger logger;
        private ArrayList<String> targets;

        public Worker(Path source,  WatchService service) throws IOException {
            this.source = source;
            this.service = service;
            this.key = source.register(service, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        }

        public void setLogger(Logger logger) {
            this.logger = logger;
        }

        public void setTargets(ArrayList<String> targets) {
            this.targets = targets;
        }

        @Override
        public void run() {
            logger.info(String.join(" | ", targets));
            while(true){
                for(WatchEvent event : key.pollEvents()){
                    Path file = source.resolve((Path) event.context());
                    info(file + " was modified");

                    for(String target : targets){
                        File sourceFile = file.toFile();
                        File targetFile = new File(target + File.separator + sourceFile.getName());
                        if(event.kind() == StandardWatchEventKinds.ENTRY_DELETE){

                            logger.log("DELETE "+ targetFile.getAbsolutePath() + " Result: " + (targetFile.delete()?"borrado": "error"), "WatcherWorker.Worker.delete");
                        }else{
                            try {
                                Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                logger.log(e.getMessage(), "WatcherWorker.Worker.Copy");
                            }
                        }

                    }
                }
            }
        }

        private void log(String text){
            if(logger != null){
                logger.log(text, "WatcherWorker.Worker");
            }
        }

        private void error(String text){
            if(logger != null){
                logger.error(text, "WatcherWorker.Worker");
            }
        }

        private void warn(String text){
            if(logger != null){
                logger.warn(text, "WatcherWorker.Worker");
            }
        }

        private void info(String text){
            if(logger != null){
                logger.info(text, "WatcherWorker.Worker");
            }
        }

        public Path getSource() {
            return source;
        }

        public void setSource(Path source) {
            this.source = source;
        }

        public WatchKey getKey() {
            return key;
        }

        public void setKey(WatchKey key) {
            this.key = key;
        }

        public WatchService getService() {
            return service;
        }

        public void setService(WatchService service) {
            this.service = service;
        }
    }
}
