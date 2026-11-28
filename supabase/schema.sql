-- 1. Perfis ligados à autenticação nativa da Supabase
CREATE TABLE perfil_utilizador (
  id_utilizador UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  email TEXT NOT NULL,
  nome TEXT
);

-- 2. Equipas e membros
CREATE TABLE equipa (
  id_equipa SERIAL PRIMARY KEY,
  nome TEXT NOT NULL
);

CREATE TABLE membro_equipa (
  id_utilizador UUID REFERENCES perfil_utilizador(id_utilizador) ON DELETE CASCADE,
  id_equipa INT REFERENCES equipa(id_equipa) ON DELETE CASCADE,
  PRIMARY KEY (id_utilizador, id_equipa)
);

-- 3. Projetos e itens
CREATE TABLE projeto (
  id_projeto SERIAL PRIMARY KEY,
  id_equipa INT REFERENCES equipa(id_equipa) ON DELETE CASCADE,
  nome TEXT NOT NULL,
  data_entrega DATE
);

CREATE TABLE item (
  id_item SERIAL PRIMARY KEY,
  id_projeto INT REFERENCES projeto(id_projeto) ON DELETE CASCADE,
  conteudo TEXT NOT NULL,
  concluido BOOLEAN DEFAULT FALSE
);

-- 4. Moldes e categorias
CREATE TABLE categoria_molde (
  id_categoria SERIAL PRIMARY KEY,
  nome_categoria TEXT NOT NULL
);

CREATE TABLE molde (
  id_molde SERIAL PRIMARY KEY,
  id_categoria INT REFERENCES categoria_molde(id_categoria) ON DELETE SET NULL,
  creditos TEXT,
  path_imagem TEXT,
  path_documento_pdf TEXT,
  dificuldade TEXT,
  favorito BOOLEAN DEFAULT FALSE
);

-- 5. Tecidos e características
CREATE TABLE caracteristicas_tecido (
  id_caracteristicas_tecido SERIAL PRIMARY KEY,
  composicao TEXT,
  caimento TEXT,
  elasticidade TEXT,
  transparencia TEXT,
  amarrotabilidade TEXT,
  durabilidade TEXT
);

CREATE TABLE tecido (
  id_tecido SERIAL PRIMARY KEY,
  id_caracteristicas_tecido INT REFERENCES caracteristicas_tecido(id_caracteristicas_tecido) ON DELETE SET NULL,
  nome TEXT NOT NULL,
  path_documento_pdf TEXT,
  path_imagem TEXT,
  favorito BOOLEAN DEFAULT FALSE
);

-- 6. Cores
CREATE TABLE cor (
  id_cor SERIAL PRIMARY KEY,
  nome TEXT NOT NULL,
  codigo_hex TEXT NOT NULL,
  favorito BOOLEAN DEFAULT FALSE
);

-- 7. Tabelas associativas (N:N com projeto)
CREATE TABLE molde_projeto (
  id_molde INT REFERENCES molde(id_molde) ON DELETE CASCADE,
  id_projeto INT REFERENCES projeto(id_projeto) ON DELETE CASCADE,
  PRIMARY KEY (id_molde, id_projeto)
);

CREATE TABLE tecido_projeto (
  id_tecido INT REFERENCES tecido(id_tecido) ON DELETE CASCADE,
  id_projeto INT REFERENCES projeto(id_projeto) ON DELETE CASCADE,
  PRIMARY KEY (id_tecido, id_projeto)
);

CREATE TABLE cor_projeto (
  id_cor INT REFERENCES cor(id_cor) ON DELETE CASCADE,
  id_projeto INT REFERENCES projeto(id_projeto) ON DELETE CASCADE,
  PRIMARY KEY (id_cor, id_projeto)
);