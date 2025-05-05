package dev.kkl.bookingsystem.base.config;

import dev.kkl.bookingsystem.auth.entity.UserPackage;
import dev.kkl.bookingsystem.auth.repository.UserPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PackageCleanupJob {

    @Autowired
    private UserPackageRepository userPackageRepository;

    @Scheduled(cron = "*/10 * * * * ?")
    public void cleanExpiredPackages() {
        LocalDateTime now = LocalDateTime.now();
        List<UserPackage> expired = userPackageRepository.findByExpiryDateBefore(now);
        for (UserPackage pkg : expired) {
            pkg.setRemainingCredits(0);
            userPackageRepository.save(pkg);
        }
    }
}
