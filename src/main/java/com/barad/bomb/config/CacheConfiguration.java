package com.barad.bomb.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.barad.bomb.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.barad.bomb.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.barad.bomb.domain.User.class.getName());
            createCache(cm, com.barad.bomb.domain.Authority.class.getName());
            createCache(cm, com.barad.bomb.domain.User.class.getName() + ".authorities");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".branchs");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".criticisms");
            createCache(cm, com.barad.bomb.domain.BranchEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.CriticismEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".files");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".writedComments");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".audienceComments");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".foodTypes");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".children");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".contacts");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".addresses");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".menuItems");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".produceFoods");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".desginFoods");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".buyerFactors");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".sellerFactors");
            createCache(cm, com.barad.bomb.domain.AddressEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.AddressEntity.class.getName() + ".factors");
            createCache(cm, com.barad.bomb.domain.ContactEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.PartnerEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.PartnerEntity.class.getName() + ".parties");
            createCache(cm, com.barad.bomb.domain.PersonEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.PersonEntity.class.getName() + ".parties");
            createCache(cm, com.barad.bomb.domain.PartyInformationEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.CommentEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.CommentEntity.class.getName() + ".children");
            createCache(cm, com.barad.bomb.domain.ClassTypeEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.ClassTypeEntity.class.getName() + ".classifications");
            createCache(cm, com.barad.bomb.domain.ClassificationEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.FoodTypeEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.FoodTypeEntity.class.getName() + ".foods");
            createCache(cm, com.barad.bomb.domain.FoodEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.FoodEntity.class.getName() + ".menuItems");
            createCache(cm, com.barad.bomb.domain.FoodEntity.class.getName() + ".factorItems");
            createCache(cm, com.barad.bomb.domain.FoodEntity.class.getName() + ".materials");
            createCache(cm, com.barad.bomb.domain.FactorEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.FactorEntity.class.getName() + ".factorItems");
            createCache(cm, com.barad.bomb.domain.FactorItemEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.MenuItemEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.PriceHistoryEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.FactorStatusHistoryEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.ConsumeMaterialEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.FileDocumentEntity.class.getName());
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".moreInfos");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".writtenComments");
            createCache(cm, com.barad.bomb.domain.PartyEntity.class.getName() + ".designedFoods");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
