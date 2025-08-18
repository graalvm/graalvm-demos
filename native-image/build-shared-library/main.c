/*
 * Copyright (c) 2023, 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

#include <stdio.h>
#include <stdlib.h>

#include "libenvmap.h"

int main(int argc, char **argv) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <filter>\n", argv[0]);
        exit(1);
    }

    graal_isolate_t *isolate = NULL;
    graal_isolatethread_t *thread = NULL;

    if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
        fprintf(stderr, "initialization error\n");
        return 1;
    }

    printf("Number of entries: %d\n", filter_env(thread, argv[1]));

    graal_tear_down_isolate(thread);
}
