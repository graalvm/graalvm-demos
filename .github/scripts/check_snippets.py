#!/usr/bin/env python
from glob import glob
from os import PathLike
from os.path import dirname, join
from typing import cast

import re


STRIP_WHITESPACE_RE = re.compile(r'\s*')
INDENT_RE = re.compile(r'^', re.MULTILINE)


def get_file_snippets(path: str | PathLike[str]):
    from marko.block import FencedCode
    from marko.inline import CodeSpan, RawText
    from marko.ext.gfm import gfm
    from marko.ext.gfm.elements import Paragraph

    with open(path) as f:
        doc = gfm.parse(f.read())
    codeblocks: list[tuple[str, str]] = []
    for idx, child in enumerate(doc.children):
        if idx > 0:
            if child.get_type() == "FencedCode":
                if (p := doc.children[idx - 1]).get_type() == "Paragraph":
                    children = cast(Paragraph, p).children
                    if len(children) == 1 and (filename := children[0]).get_type() == "CodeSpan":
                        code = cast(RawText, cast(FencedCode, child).children[0]).children
                        codeblocks.append((cast(str, cast(CodeSpan, filename).children), code))
    return codeblocks


def match_snippets(snippets, rootdir):
    unmatched_snippets: list[tuple[list[str], str]] = []
    for filename, snippet in snippets:
        stripped_snippet = STRIP_WHITESPACE_RE.sub("", snippet)
        files = glob(join(rootdir, "**", filename), recursive=True)
        for filepath in files:
            with open(filepath) as f:
                contents = f.read()
            contents = STRIP_WHITESPACE_RE.sub("", contents)
            if stripped_snippet in contents:
                break
        else:
            unmatched_snippets.append((files, snippet))
    return unmatched_snippets


if __name__ == "__main__":
    import sys
    from argparse import ArgumentParser

    parser = ArgumentParser(__file__)
    parser.add_argument("filename", nargs='+')
    args = parser.parse_args()

    failed = False
    for filename in args.filename:
        snippets = get_file_snippets(filename)
        unmatched_snippets = match_snippets(snippets, dirname(filename))
        print(f"Testing {filename}")
        for files, snippet in unmatched_snippets:
            failed = True
            print("Failed to match the following snippet:", INDENT_RE.sub("\t", snippet), f"Tried these files: {files}", sep="\n")

    sys.exit(failed)
