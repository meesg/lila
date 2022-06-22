import { path as treePath } from 'tree';
import { KeyboardController } from './interfaces';

export function canGoForward(ctrl: KeyboardController): boolean {
  return ctrl.vm.node.children.length > 0;
}

export function next(ctrl: KeyboardController): void {
  const child = ctrl.vm.node.children[0];
  if (!child || child.disabled === true) return;
  ctrl.userJump(ctrl.vm.path + child.id);
}

export function prev(ctrl: KeyboardController): void {
  ctrl.userJump(treePath.init(ctrl.vm.path));
}

function removeDisabled(path: Tree.Node[]): Tree.Node[] {
  let cPath = path

  while (cPath[cPath.length - 1].disabled === true) {
    cPath.splice(-1, 1)
  }
  
  return cPath
}

export function last(ctrl: KeyboardController): void {
  const toInit = !treePath.contains(ctrl.vm.path, ctrl.vm.initialPath);
  ctrl.userJump(toInit ? ctrl.vm.initialPath : treePath.fromNodeList(removeDisabled(ctrl.vm.mainline)));
}

export function first(ctrl: KeyboardController): void {
  const toInit = ctrl.vm.path !== ctrl.vm.initialPath && treePath.contains(ctrl.vm.path, ctrl.vm.initialPath);
  ctrl.userJump(toInit ? ctrl.vm.initialPath : treePath.root);
}
