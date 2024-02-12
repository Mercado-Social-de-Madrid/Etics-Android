package net.mercadosocial.moneda.api;

import net.mercadosocial.moneda.model.Node;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by julio on 1/02/18.
 */

public interface NodesApi {

    @GET("nodes/")
    Observable<Response<List<Node>>> getNodes();


}
