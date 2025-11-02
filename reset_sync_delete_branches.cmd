@echo off
git fetch --all --prune

for /f "delims=" %%A in ('git branch --format "%%(refname:short)"') do (
    git show-ref --verify --quiet refs/remotes/origin/%%A
    if not errorlevel 1 (
        echo Syncing branch %%A with origin/%%A...
        git checkout %%A
        git reset --hard origin/%%A
    ) else (
        echo no matching remote branch delete: %%A
        git branch -D %%A
    )
)

git checkout main
echo All branches are synced with origin.
pause