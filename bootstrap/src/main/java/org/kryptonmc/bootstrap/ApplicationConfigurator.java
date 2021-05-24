package org.kryptonmc.bootstrap;

import com.google.gson.Gson;
import io.github.slimjar.app.ApplicationConfiguration;
import io.github.slimjar.downloader.DependencyDownloader;
import io.github.slimjar.downloader.URLDependencyDownloader;
import io.github.slimjar.downloader.output.DependencyFileOutputWriterFactory;
import io.github.slimjar.downloader.output.OutputWriterFactory;
import io.github.slimjar.downloader.strategy.FilePathStrategy;
import io.github.slimjar.injector.DependencyInjector;
import io.github.slimjar.injector.DownloadingDependencyInjector;
import io.github.slimjar.resolver.CachingDependencyResolver;
import io.github.slimjar.resolver.DependencyResolver;
import io.github.slimjar.resolver.data.DependencyData;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import io.github.slimjar.resolver.enquirer.SimpleRepositoryEnquirerFactory;
import io.github.slimjar.resolver.mirrors.MirrorSelector;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import io.github.slimjar.resolver.pinger.HttpURLPinger;
import io.github.slimjar.resolver.reader.DependencyDataProviderFactory;
import io.github.slimjar.resolver.strategy.MavenPathResolutionStrategy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public final class ApplicationConfigurator {

    private static final Gson GSON = new Gson();
    private static final String APPLICATION_NAME = "krypton";
    private static final URL DEPENDENCY_FILE_URL = Thread.currentThread().getContextClassLoader().getResource("slimjar.json");
    private static final File DOWNLOAD_DIRECTORY = new File("libraries");

    public static ApplicationConfiguration configure() throws IOException {
        final DependencyData data = new DependencyDataProviderFactory(GSON).forFile(DEPENDENCY_FILE_URL).get();
        final MirrorSelector mirrorSelector = new SimpleMirrorSelector();
        final Collection<Repository> repositories = mirrorSelector.select(data.getRepositories(), data.getMirrors());
        final FilePathStrategy filePathStrategy = FilePathStrategy.createDefault(DOWNLOAD_DIRECTORY);
        final FilePathStrategy relocationFilePathStrategy = FilePathStrategy.createRelocationStrategy(DOWNLOAD_DIRECTORY, APPLICATION_NAME);
        final OutputWriterFactory outputWriterFactory = new DependencyFileOutputWriterFactory(filePathStrategy, relocationFilePathStrategy, new DummyRelocator());
        final RepositoryEnquirerFactory repositoryEnquirerFactory = new SimpleRepositoryEnquirerFactory(new MavenPathResolutionStrategy(), new HttpURLPinger());
        final DependencyResolver resolver = new CachingDependencyResolver(repositories, repositoryEnquirerFactory);
        final DependencyDownloader downloader = new URLDependencyDownloader(outputWriterFactory, resolver);
        final DependencyInjector injector = new DownloadingDependencyInjector(downloader);
        return new ApplicationConfiguration(injector, data);
    }
}
