# Java Dependency Extractor

The Java Dependency Extractor (jde) analyzes source code repositories and extracts code dependencies at each commit.


## Dependency Types
Supported:

- method call dependencies

Planned:

- points to dependencies
- data dependencies
- plug-in like dependencies

## Repositories
Supported:

- git

Planned:

- hg
- svn
- cvs

## Output formats
Supported:

- XML

Planned:

- PostGreSQL
- MySQL
- JSON

## Concurrency Features
Supported:

- parallel processing of Java ASTs
- async data output

Planned:

- process multiple commits in parallel

## Misc Features
Supported:

- ignore folders

Planned:

- support maven projects
- support clone/checkout for remote repositories

# Usage


`java -jar jde.jar [options] pathToRepository`

options:

    -r type | --repository=type
	   sepcifies the type of repository the source is contained, currently supported repos are git (default).
    -f type | --output-format=type
       specifies the format of the output, currently supported are xml (default).
    -i regexp | --ignore=regexp
       specifies regular expression of foldernames that should be ignored.
    -q size | --queue-size=size
       specifies the size of the output queue (default: 10) (-1: no limit).