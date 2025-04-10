{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };
  outputs = { nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {inherit system;};
      in {
        devShell = pkgs.mkShell {
          nativeBuildInputs = with pkgs; [ jdk maven ];
        };
        packages.default = pkgs.maven.buildMavenPackage {
          pname = "cardio_generator";
          version = "1.0.0";
          src = ./.;
          mvnHash = "sha256-rcEXEqruIKVnV1jzbLidkCXgs+cr+0CQzvnXZvvuzcY=";
          nativeBuildInputs = [ pkgs.makeWrapper ];
          installPhase = ''
            mkdir -p $out/bin $out/share/cardio_generator
            cp target/cardio_generator-1.0-SNAPSHOT.jar $out/share/cardio_generator/cardio_generator.jar
            makeWrapper ${pkgs.jre_minimal}/bin/java $out/bin/cardio-generator \
              --add-flags "-jar $out/share/cardio_generator/cardio_generator.jar"
          '';
        };
      }
    );
}
