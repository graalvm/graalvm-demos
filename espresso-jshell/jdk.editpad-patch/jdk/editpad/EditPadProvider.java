package jdk.editpad;

// Patched to purge the AWT optional dependency from jshell.
// Note: --patch-module cannot "patch" existing module-info.class, so the (editor) provider is nop-ed instead.
abstract class EditPadProvider {
}
