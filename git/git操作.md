## git命令

```bash
git clone https://github.com/wangkang09/JavaTest.git # 克隆远程仓库到本地
git clone -o configRepo https://github.com/wangkang09/config-repo.git # 克隆并指定主机名为configRepo，默认是origin

# 获取仓库状态
git status 

# 将工作区的文件放入暂存区
git add <file/directory> 

# 将暂存区的文件放入版本库
git commit -m "提交说明" 
# 将所有提交文件，放入版本库
git commit -am "提交说明" 

# 获取历史提交记录
git log 
# 同上
git log --pretty=oneline 

# 将版本回退到上一个版本，仅仅是指针的变化，很快
git reset --hard HEAD^ 
# 将版本回退到特定的版本号
git reset --hard <版本号> 

# 查询每一次命令的版本号，方便回退
git reflog 

# 查看工作区和暂存区的区别
git diff readme.txt 
# 查看工作区和版本库的区别
git diff HEAD readme.txt 

# 当你不小心改错了工作区的内容时，想丢弃工作区的修改。如果没有 -- ，是切换分支的命令
git checkout -- readme.txt 

# 当你不小心改错了工作区的内容，并且添加到了暂存区，想丢弃。先使用这个命令，用版本库的内容覆盖暂存区，在使用上面的命令，用暂存区的覆盖工作区。也可以直接提交后，在回退git reset --hard HEAD^
git reset HEAD readme.txt 

# 从工作区删除readme.txt文件
rm readme.txt 
# 提交到暂存区
git add/rm readme.txt 
# 等于上面两步操作
git rm readme.txt 

# 创建分支，仅仅是改下dev的指针指向，当前分支最新版本
git branch dev 
# 切换到dev分支
git checkout dev 
# 相当于以上两个步骤
git checkout -b dev 

# 查看分支和当前分区，标`*`号的是当前分支
git branch 
# 合并某个分支到当前分支中
git merge <branchName> 
git fetch origin test

# 合并两个远程分支，将dev分支的内容合并到test分支中
git checkout -b dev origin/dev
git checkout test
git merge dev # 将dev合并到test中
git push # 将test分支提交到远程

# 删除某个分支
git branch -d <branchName> 

#查看分支合并图
git log --graph 

# 存储工作现场，经过这个命令后，工作区就是干净的了
# 作用：当临时有个bug要修复，先把工作现场stash一下，在创建bug分支修复，这样就不会提示未提交的信息
git stash
# 查看存储的工作现场
git stash list 
# 恢复工作现场，并将stash删除
git stash pop 
git stash apply + git stash drop


# 在本地创建和远程分支对应的分支
git fetch origin 
git checkout -b branchName origin/branchName

# 将远程的test分支fetch到本地的example分支
git fetch origin test:example

# git push，推送本地分支到远程分支
git push <远程主机名> <本地分支名>:<远程分支名>
git push origin test # 表示将本地分支test推送到远程主机的test分支中，如果后者不存在，则会被新建

git push origin # 将当前分支推送到远程origin的同名（有追踪关系）分支
git push # 如果当前分支只有一个追踪分支，远程主机名可以不写

# 删除远程分支
git push origin :test # 删除远程test分支
git push origin --delete test # 同上


# git pull
git pull <远程主机> <远程分支>:<本地分支>
git pull origin master:test # 将远程master分支，拉取并合并到本地test分支中

git pull origin master # 将远程master分支，拉取并合并到当前分支

```







## git重要概念

**工作区：**就是我们运行`git init`时，所在的路径，运行后会产生一个`.git`的隐藏文件

**版本库：**产生的隐藏文件就是`Git`的版本库

* `index`：暂存区，又称`stage`，很重要
* `HEAD`：指向`master`的指针



**版本管理流程**

* 当我们在工作区增加文件后，使用`git status`命令，会出现以下显示，说明工作区的readme.txt文件没有被版本库跟踪
```bash
  Administrator@PC-20180521MXYH MINGW64 /d/gitTest (master)
  $ git status
  On branch master
  Untracked files: # 文件没有跟踪
    (use "git add <file>..." to include in what will be committed)
          readme.txt
```

* 使用`git add readme.txt`命令，将工作区的文件**放入暂存区**后，使用`git status`，说明文件已被跟踪，需要commit到版本库中
```bash
  Administrator@PC-20180521MXYH MINGW64 /d/gitTest (master)
  $ git status
  On branch master # 这就是当前的版本库
  Changes to be committed:
    (use "git reset HEAD <file>..." to unstage)
          new file:   readme.txt
```

* 使用`git commit readme.txt -m "添加一个readme.txt到版本库中"`命令，将暂存区文件，放入版本库，这里的版本库是`master`
```bash
Administrator@PC-20180521MXYH MINGW64 /d/gitTest (master)
$ git commit readme.txt -m "添加一个readme.txt到版本库中"
warning: LF will be replaced by CRLF in readme.txt.
The file will have its original line endings in your working directory. # 这个是windows和linux换行符的差异，现在可忽略
[master 5e9a8b7] 添加一个readme.txt到版本库中
 1 file changed, 1 insertion(+) # 显示master中新增了一个文件
 create mode 100644 readme.txt
```



参考：

https://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000