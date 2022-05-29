package wuttifrutti.beerchef

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication
class BeerchefApplication {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			SpringApplication.run(BeerchefApplication::class.java, *args)
		}
	}

	@Bean
	fun corsConfigurer(): WebMvcConfigurer {
		return object : WebMvcConfigurer {
			override fun addCorsMappings(registry: CorsRegistry) {
				registry.addMapping("/**").allowedOrigins("http://192.168.178.108:19006").allowCredentials(true).allowedMethods("GET", "POST","PUT", "DELETE", "OPTIONS","HEAD")
			}
		}
	}

}


