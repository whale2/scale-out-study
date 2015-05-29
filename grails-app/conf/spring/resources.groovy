import tstapp.CouchBaseConnectorService

// Place your Spring DSL code here
beans = {
    couchBucket(CouchBaseConnectorService)
    fillBuckets(FillBuckets) { bean ->
        bean.autowire = 'byName'
    }
}
