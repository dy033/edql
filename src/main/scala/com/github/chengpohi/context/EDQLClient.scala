package com.github.chengpohi.context

import org.elasticsearch.client.RestClient


class EDQLClient(rc: RestClient, kb: Boolean = false, ro: Boolean = false, ps: String = "") {
  val restClient: RestClient = rc
  val kibanaProxy: Boolean = kb
  val readOnly: Boolean = ro
  val pathPrefix: String = ps
}

object EDQLClient {
  def apply(rc: RestClient, kibanaProxy: Boolean = false, readOnly: Boolean = false, pathPrefix: String = ""): EDQLClient =
    new EDQLClient(rc, kibanaProxy, readOnly, pathPrefix)
}
