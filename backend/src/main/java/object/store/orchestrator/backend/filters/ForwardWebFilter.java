package object.store.orchestrator.backend.filters;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.http.server.PathContainer.Element;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchange.Builder;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class ForwardWebFilter implements WebFilter {
  public static final List<String> REST_PATHS = Collections.emptyList();

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    List<Element> pathList = exchange.getRequest().getPath().elements();
    boolean isRestPath = pathList.stream().anyMatch( element -> REST_PATHS.contains(element.value()));
    if(isRestPath){
      return chain.filter(exchange);
    }else{
      long count = pathList.size();
      Optional<Element> lastElement = pathList.stream().skip(count - 1).findFirst();
      String path = "/index.html";
      if(lastElement.isPresent()){
        if(lastElement.get().value().contains(".")){
          path = "/" + lastElement.get().value();
        }
      }
      ServerHttpRequest requestModified = exchange
          .getRequest()
          .mutate()
          .path(path)
          .build();

      Builder request = exchange
          .mutate()
          .request(requestModified);

      return chain.filter(request.build());
    }
  }
}
