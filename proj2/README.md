# Gitlet - Simple version of Git

This simple version of Git (Gitlet) is a big homework project in CS61B. Gitlet have some part of the same support commands like the real Git i.e `init`, `add`, `commit`, `checkout` (switch to different branch) .... 

People can use Gitlet to back to the older version of the tracked folder, and give every different tracked versions a specific version message.  

## Persistence Folder Structure (.gitlet)
The persistence folder `.gitlet` is for storing the current gitlet status and for each commit information.  

Below is the overall structure of the `.getlet` folder:

+ .gitlet
  + `HEAD` - Store the current branch HEAD reference e.g `refs/heads/` 
  + `index` - Store the staging area information to check if there are any differents between working directory, staging area,    
  + refs 
    + heads - Store the  
      + `master` - Store a single line of commit SHA-1 hashed name which HEAD pointer point to in master branch. e.g `4c0b...` 
      + `branch2`
      + `branch3`
      + ...
    + tags - (Not used) Store tags to commits  
  + objects - Store each commit information and every files with BLOB version (used the commit sha-1 hash name `4c0b...` for example)  
    + 4c
      + `0b` - Store the commit information

## TODOs for each commands
### `init`
Initialize the `.gitlet` folder to the current folder
1. Create `.gitlet` folder
2. Create the following folder and files `refs/heads/master`, `objects`, `HEAD`, `index`
3. Create the initial commit, which do not contain files and with the commit message "initial commit"
4. Initialize the content for  `refs/heads/master` and `HEAD` (HEAD will point to the initial commit)

### `add`
Add a specific file (can only a file per time) into staging area.
1. Write the target file into `.gitlet/objects`
2. Write the target file into `.gitlet/index` to track the file state

### `commit`
Snapshot the current folder state, give the snapshot a brief message and store the snapshot.
1. Create a COMMIT object, which contains `message`, `date`, `parent1`, `parent2`, `map` (this is the map to store the file original name and its Java File)
2. Set the current `HEAD` commit as the parent COMMIT (`parent1`), the first  
3. TODO: How to handle merge? How to set the second parent? 
4. Inherit the parent COMMIT file map as default. Just only have to update some files that are modified or added in this commit (check `.gitlet/index`)  
5. Write this COMMIT into `.gitlet/objects`
6. SET the `.gitlet/HEAD` to the current COMMIT

### `status`
Print out the current branch, staged files, removed files, modification and untracked files.
1. See the HEAD pointer reference inside the `HEAD` to get the current branch
2. When call the `status` script, iterate every files in the `.gitlet` to check each file current status

### How to store the staging area information inside index
reference: [Understanding Git — Index](https://konrad126.medium.com/understanding-git-index-4821a0765cf)





